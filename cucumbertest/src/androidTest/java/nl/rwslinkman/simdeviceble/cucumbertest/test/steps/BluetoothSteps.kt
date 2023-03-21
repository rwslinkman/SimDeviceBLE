package nl.rwslinkman.simdeviceble.cucumbertest.test.steps

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

    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private val scanResults: MutableList<ScanResult> = mutableListOf()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Log.i(TAG, "onScanResult")
            result?.let {
                scanResults.add(it)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.i(TAG, "onBatchScanResults: ")
        }

        override fun onScanFailed(errorCode: Int) {
            Log.i(TAG, "onScanFailed: ")
        }
    }

    @Given("I have configured the Bluetooth scanner")
    fun setupBleStuff() {
        scenario.launchTestActivity()
        bluetoothLeScanner = scenario.setupBluetoothScanner()
    }

    @When("I start a BLE discovery for {int} seconds")
    fun startBleDiscovery(duration: Int) = runBlocking {
        if(bluetoothLeScanner == null) throw IllegalStateException("Setup not completed. Add 'Given I have configured the Bluetooth scanner' to the scenario")

        bluetoothLeScanner?.startScan(scanCallback)
        delay(duration * 1000L)
        bluetoothLeScanner?.stopScan(scanCallback)
    }

    @Then("it should find the target BLE device")
    fun shouldFindDevices() {
        assertTrue(scanResults.isNotEmpty())
        val targetScanResult = scanResults.find { it.device.name == targetDeviceName }
        assertNotNull(targetScanResult)
    }

    companion object {
        private const val TAG = "BluetoothSteps"
        private const val targetDeviceName = "Lenovo Tab P11 Pro"
    }

    @After
    fun stopBluetoothScan() = bluetoothLeScanner?.stopScan(scanCallback)
}