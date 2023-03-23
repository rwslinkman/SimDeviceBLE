package nl.rwslinkman.simdeviceble.cucumbertest.test

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import io.cucumber.java.After
import nl.rwslinkman.simdeviceble.cucumbertest.CucumberTestActivity

class ActivityScenarioHolder {
    private var scenario: ActivityScenario<*>? = null

    val appContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    fun launchTestActivity() = launch(Intent(
        InstrumentationRegistry.getInstrumentation().targetContext,
        CucumberTestActivity::class.java
    ))

    fun setupBluetoothScanner(): BluetoothAdapter {
        val bluetoothManager = appContext.getSystemService(BluetoothManager::class.java)

        // TODO: Permission checks
        return bluetoothManager.adapter ?: throw IllegalStateException("No BLE support")
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
}