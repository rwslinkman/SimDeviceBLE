package nl.rwslinkman.simdeviceble.cucumbertest.test.steps

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import io.cucumber.java.After
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nl.rwslinkman.simdeviceble.cucumbertest.test.ActivityScenarioHolder
import org.junit.Assert.*

class BluetoothSteps
{
    private val scenario: ActivityScenarioHolder = ActivityScenarioHolder()

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private val scanResults: MutableList<ScanResult> = mutableListOf()
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.i(TAG, "onScanResult")
            result?.let { scanResults.add(it) }
        }
    }
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            // TODO
        }
    }

    private var targetScanResult: ScanResult? = null

    @Given("I have configured the Bluetooth scanner")
    fun setupBleStuff() {
        scenario.launchTestActivity()
        bluetoothAdapter = scenario.setupBluetoothScanner()
        bluetoothLeScanner = bluetoothAdapter!!.bluetoothLeScanner
    }

    @When("I start a BLE discovery for {int} seconds")
    fun startBleDiscovery(duration: Int) = runBlocking {
        if(bluetoothLeScanner == null) throw IllegalStateException("Setup not completed. Add 'Given I have configured the Bluetooth scanner' to the scenario")

        scanResults.clear()
        bluetoothLeScanner?.startScan(scanCallback)
        delay(duration * 1000L)
        bluetoothLeScanner?.stopScan(scanCallback)
    }

    @Then("it should find the {string} BLE device")
    fun shouldFindDevices(targetDeviceName: String) {
        assertTrue(scanResults.isNotEmpty())
        val targetScanResult = scanResults.find { it.device.name == targetDeviceName }
        assertNotNull("Target device was not found among scan results", targetScanResult)
    }

    @Then("it has found the {string} device advertising the {string} service UUID")
    fun verifyDevicePrimaryServiceUUID(deviceName: String, serviceName: String) {
        val expectedPrimaryServiceUUID = bleGattServiceMap[serviceName]
        assertNotNull(expectedPrimaryServiceUUID)
        assertNotEquals("", expectedPrimaryServiceUUID)

        assertTrue(scanResults.isNotEmpty())
        targetScanResult = scanResults.find { it.device.name == deviceName }
        assertNotNull("Target device was not found among scan results", targetScanResult)
        val serviceUUIDs = targetScanResult?.scanRecord?.serviceUuids
        assertNotNull(serviceUUIDs)
        assertTrue(serviceUUIDs!!.isNotEmpty())

        val actualServiceUUID = serviceUUIDs.find { it.uuid.toString() == expectedPrimaryServiceUUID }
        assertNotNull(actualServiceUUID)
    }

    @Then("the advertising data of the target device contains stuff")
    fun verifyTargetScanResult() {
        assertNotNull(targetScanResult)
        Log.i(TAG, "verifyTargetScanResult: verify ${targetScanResult?.device?.name}")
    }

    @When("I connect to the target device")
    fun connectToTargetDevice() {
        assertNotNull(bluetoothAdapter)
        assertNotNull(targetScanResult)
        val targetDevice = bluetoothAdapter!!.getRemoteDevice(targetScanResult!!.device.address)
        targetDevice.connectGatt(scenario.appContext, false, gattCallback)
    }

    companion object {
        private const val TAG = "BluetoothSteps"
        private val bleGattServiceMap: Map<String, String> = mapOf(
            "Health Thermometer" to bleUUID("1809"),
            "Current Time" to bleUUID("1805")
        )

        private fun bleUUID(shortUUID: String) = "0000${shortUUID}-0000-1000-8000-00805f9b34fb"
    }

    @After
    fun stopBluetoothScan() = bluetoothLeScanner?.stopScan(scanCallback)
}