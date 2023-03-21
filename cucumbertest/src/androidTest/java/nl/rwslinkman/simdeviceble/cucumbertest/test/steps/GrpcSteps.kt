package nl.rwslinkman.simdeviceble.cucumbertest.test.steps

import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import io.cucumber.java.After
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nl.rwslinkman.simdeviceble.cucumbertest.test.ActivityScenarioHolder

class GrpcSteps
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

    @Given("I am saying {string} to the console")
    fun givenSayingToConsole(words: String) {
        scenario.launchTestActivity()
        Log.i(TAG, "givenSayingToConsole: step says $words")
    }

    @Given("I have configured the Bluetooth scanner")
    fun setupBleStuff() {
        scenario.launchTestActivity()
        bluetoothLeScanner = scenario.setupBluetoothScanner()
    }

    @When("I start a BLE discovery for {int} seconds")
    fun startBleDiscovery(duration: Int) = runBlocking {
        if(bluetoothLeScanner == null) throw IllegalStateException("Setup not completed. ")

        bluetoothLeScanner?.startScan(scanCallback)
        delay(duration * 1000L)
        bluetoothLeScanner?.stopScan(scanCallback)
    }

    @Then("it should find some devices")
    fun shouldFindDevices() {
        Log.i(TAG, "shouldFindDevices: Discovery end")
        assertTrue(scanResults.isNotEmpty())
    }

    companion object {
        private const val TAG = "GrpcSteps"
    }

    @After
    fun stopBluetoothScan() = bluetoothLeScanner?.stopScan(scanCallback)
}