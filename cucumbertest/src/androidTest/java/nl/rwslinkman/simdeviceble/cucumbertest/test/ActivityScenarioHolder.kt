package nl.rwslinkman.simdeviceble.cucumbertest.test

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Intent
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import io.cucumber.java.After
import nl.rwslinkman.simdeviceble.cucumbertest.CucumberTestActivity
import nl.rwslinkman.simdeviceble.cucumbertest.test.steps.GrpcSteps

class ActivityScenarioHolder {
    private var scenario: ActivityScenario<*>? = null

    fun launchTestActivity() = launch(Intent(
        InstrumentationRegistry.getInstrumentation().targetContext,
        CucumberTestActivity::class.java
    ))

    fun setupBluetoothScanner(): BluetoothLeScanner? {
        val app = InstrumentationRegistry.getInstrumentation().targetContext
        val bluetoothManager = app.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.i(TAG, "setupBleStuff: No BLE support")
            return null
        }

        return bluetoothAdapter.bluetoothLeScanner
    }

    private fun launch(intent: Intent){
        scenario = ActivityScenario.launch<Activity>(intent)
    }

    /**
     *  Close activity after scenario
     */
    @After
    fun close(){
        scenario?.close()
    }

    companion object {
        private const val TAG = "ActivityScenarioHolder"
    }
}