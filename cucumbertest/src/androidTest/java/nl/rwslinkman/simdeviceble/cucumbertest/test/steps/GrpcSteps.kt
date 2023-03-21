package nl.rwslinkman.simdeviceble.cucumbertest.test.steps

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.delay
import nl.rwslinkman.simdeviceble.cucumbertest.CucumberTestActivity
import nl.rwslinkman.simdeviceble.cucumbertest.test.ActivityScenarioHolder


class GrpcSteps
{
    private val scenario: ActivityScenarioHolder = ActivityScenarioHolder()

    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Log.i(TAG, "onScanResult")

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
        scenario.launch(
            Intent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                CucumberTestActivity::class.java
            )
        )


        Log.i(TAG, "givenSayingToConsole: step says $words")
    }

    @Given("I have set up the BLE stuff")
    fun setupBleStuff() {

        scenario.launch(
            Intent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                CucumberTestActivity::class.java
            )
        )


        val app = InstrumentationRegistry.getInstrumentation().targetContext

        val bluetoothManager = app.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.i(TAG, "setupBleStuff: No BLE support")
            return
        }

        val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        // NOP
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner.stopScan(scanCallback)
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeScanner.startScan(scanCallback)
        } else {
            scanning = false
            bluetoothLeScanner.stopScan(scanCallback)
        }
    }

    @When("I start a BLE discovery")
    fun startBleDiscovery() {
       // TODO
    }

    @Then("it should find some devices")
    fun shouldFindDevices() {
        Log.i(TAG, "shouldFindDevices: Discovery end")
    }

    companion object {
        private const val TAG = "GrpcSteps"
    }
}