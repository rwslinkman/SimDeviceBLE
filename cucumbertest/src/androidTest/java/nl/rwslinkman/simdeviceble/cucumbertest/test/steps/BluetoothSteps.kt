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
            Log.i(TAG, "onServicesDiscovered: ")
        }

        override fun onPhyUpdate(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status)
            Log.i(TAG, "onPhyUpdate: ")
        }

        override fun onPhyRead(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyRead(gatt, txPhy, rxPhy, status)
            Log.i(TAG, "onPhyRead: ")
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            // int STATE_CONNECTED = 2;
            // int STATE_CONNECTING = 1;
            // int STATE_DISCONNECTED = 0;
            // int STATE_DISCONNECTING = 3;

            val newStateStr = when(newState) {
                BluetoothGatt.STATE_CONNECTED -> "connected"
                BluetoothGatt.STATE_CONNECTING -> "connecting"
                BluetoothGatt.STATE_DISCONNECTED -> "disconnected"
                BluetoothGatt.STATE_DISCONNECTING -> "disconnecting"
                else -> "unknown"
            }
            Log.i(TAG, "onConnectionStateChange to '$newStateStr' on BLE")
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
            Log.i(TAG, "onCharacteristicRead: ")
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.i(TAG, "onCharacteristicWrite: ")
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            Log.i(TAG, "onCharacteristicChanged: ")
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int,
            value: ByteArray
        ) {
            super.onDescriptorRead(gatt, descriptor, status, value)
            Log.i(TAG, "onDescriptorRead: ")
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Log.i(TAG, "onDescriptorWrite: ")
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
            super.onReliableWriteCompleted(gatt, status)
            Log.i(TAG, "onReliableWriteCompleted: ")
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
            Log.i(TAG, "onReadRemoteRssi: ")
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
            Log.i(TAG, "onMtuChanged: ")
        }

        override fun onServiceChanged(gatt: BluetoothGatt) {
            super.onServiceChanged(gatt)
            Log.i(TAG, "onServiceChanged: ")
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

    @When("I wait for the connection")
    fun connectDelay() = runBlocking {
        delay(5000)
        Log.i(TAG, "connectDelay: finish")
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