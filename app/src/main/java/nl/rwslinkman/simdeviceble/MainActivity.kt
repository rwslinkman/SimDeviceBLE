package nl.rwslinkman.simdeviceble

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appModel: AppModel
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var advManager: AdvertisementManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val model: AppModel by viewModels()
        appModel = model

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        model.bluetoothSupported.postValue(bluetoothAdapter != null)

        bluetoothAdapter?.let {
            model.bluetoothEnabled.postValue(it.isEnabled)
            model.bluetoothAdvertisingSupported.postValue(it.isMultipleAdvertisementSupported)
            model.advertisementName.postValue(it.name)

            // inject
            advManager = AdvertisementManager(this, it, model)
            model.advertisementManager = advManager
        }

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            // Bluetooth on/off
            val isEnabled = resultCode == RESULT_OK
            appModel.bluetoothEnabled.postValue(isEnabled)

            // Advertising supported
            val canAdvertise = bluetoothAdapter?.isMultipleAdvertisementSupported
            appModel.bluetoothAdvertisingSupported.postValue(canAdvertise)
        }
    }

    override fun onPause() {
        appModel.advertisementManager?.stop()
        appModel.advertisementManager = null // it contains context, must not last too long
        super.onPause()
    }

    fun startBluetoothIntent() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_ENABLE_BT = 1337;
    }
}