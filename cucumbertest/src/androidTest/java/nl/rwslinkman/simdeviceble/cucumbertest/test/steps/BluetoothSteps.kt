package nl.rwslinkman.simdeviceble.cucumbertest.test.steps

import android.bluetooth.*
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
import nl.rwslinkman.simdeviceble.cucumbertest.test.grpc.AdvertisedCharacteristic
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
            if (status == BluetoothGatt.GATT_SUCCESS) {
                collectGattServiceData(targetGattDevice?.services)
            }
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if(newState == BluetoothGatt.STATE_CONNECTED) {
                targetGattDevice?.discoverServices()
            }
        }
    }
    private var targetScanResult: ScanResult? = null
    private var targetGattDevice: BluetoothGatt? = null
    private val discoveredGattData: MutableMap<String, List<String>> = mutableMapOf()

    private val grpcClient = GrpcControlSteps().grpcClient // DependencyInjection would be better
    private var advertisedCharacteristics: MutableList<AdvertisedCharacteristic> = mutableListOf()

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

    @When("I connect to the target device with a {int} second timeout")
    fun connectToTargetDevice(timeout: Int) = runBlocking {
        assertNotNull(bluetoothAdapter)
        assertNotNull(targetScanResult)
        val targetDevice = bluetoothAdapter!!.getRemoteDevice(targetScanResult!!.device.address)
        discoveredGattData.clear()
        targetGattDevice = targetDevice.connectGatt(scenario.appContext, false, gattCallback)
        delay(timeout * 1000L)
    }

    @Then("it should have discovered all GATT Services on the device")
    fun verifyGattServiceDiscovery() {
        assertNotNull(targetGattDevice)
        assertNotEquals(0, discoveredGattData.size)

        val genericAccessUUID = bleGattServiceMap["Generic Access"]
        assertTrue(discoveredGattData.containsKey(genericAccessUUID))
        val genericAttributeUUID = bleGattServiceMap["Generic Attribute"]
        assertTrue(discoveredGattData.containsKey(genericAttributeUUID))
    }

    @Then("one of the discovered GATT Services is {string} with the {string} GATT Characteristic")
    fun verifyCharacteristicDiscovery(serviceName: String, characteristicName: String) {
        assertNotNull(targetGattDevice)
        assertNotEquals(0, discoveredGattData.size)

        val expectedServiceUUID = bleGattServiceMap[serviceName]
        assertNotNull(expectedServiceUUID)

        val expectedCharacteristicUUID = bleGattCharacteristicMap[characteristicName]
        assertNotNull(expectedCharacteristicUUID)

        val actualCharacteristics = discoveredGattData[expectedServiceUUID]
        assertNotNull(actualCharacteristics)
        val isCharacteristicDiscovered: Boolean = actualCharacteristics?.contains(expectedCharacteristicUUID) == true
        assertTrue(isCharacteristicDiscovered)
    }

    @When("the simulator returns a list of advertised characteristics")
    fun requestAdvertisedCharacteristicsList() {
        val result = grpcClient.listAdvertisedCharacteristics()
        assertNotNull(result)
        assertNotEquals(0, result.size)
        advertisedCharacteristics.clear()
        advertisedCharacteristics.addAll(result)
    }

    @Then("the discovered GATT Characteristics are equal to the Advertised Characteristics on gRPC")
    fun verifyGattEqualsGrpc() {
        assertNotNull(discoveredGattData)
        assertNotEquals(0, discoveredGattData)
        assertNotNull(advertisedCharacteristics)
        assertNotEquals(0, advertisedCharacteristics.size)

        val allDiscoveredCharacteristics: List<String> = discoveredGattData.flatMap { it.value }
        val allAdvertisedCharUUIDs: List<String> = advertisedCharacteristics.map { it.uuid }

        assertTrue(allDiscoveredCharacteristics.containsAll(allAdvertisedCharUUIDs))
    }

    companion object {
        private const val TAG = "BluetoothSteps"
        private val bleGattServiceMap: Map<String, String> = mapOf(
            "Generic Access" to bleUUID("1800"),
            "Generic Attribute" to bleUUID("1801"),
            "Health Thermometer" to bleUUID("1809"),
            "Current Time" to bleUUID("1805"),
            "Battery" to bleUUID("180F"),
            "Device Information" to bleUUID("180A")
        )
        private val bleGattCharacteristicMap: Map<String, String> = mapOf(
            "Current Time" to bleUUID("2A2B"),
            "Battery Level" to bleUUID("2A19"),
            "Manufacturer Name" to bleUUID("2A29"),
            "Model Number" to bleUUID("2A24"),
            "Serial Number" to bleUUID("2A25"),
            "Hardware Revision" to bleUUID("2A27"),
            "Firmware Revision" to bleUUID("2A26"),
            "Software Revision" to bleUUID("2A28"),
            "System Identifier" to bleUUID("2A23"),
            "Regulatory Certification" to bleUUID("2A2A"),
            "Pnp Identifier" to bleUUID("2A50")
        )

        private fun bleUUID(shortUUID: String) = "0000${shortUUID.lowercase()}-0000-1000-8000-00805f9b34fb"
    }

    @After
    fun stopBluetoothScan() = bluetoothLeScanner?.stopScan(scanCallback)

    @After
    fun stopBluetoothGatt() = targetGattDevice?.disconnect()

    private fun collectGattServiceData(gattServices: List<BluetoothGattService>?) {
        gattServices?.forEach {
            val serviceUUID = it.uuid.toString()
            val charaData = it.characteristics.map { gattCharacteristic ->
                gattCharacteristic.uuid.toString()
            }
            discoveredGattData[serviceUUID] = charaData
        }
    }
}