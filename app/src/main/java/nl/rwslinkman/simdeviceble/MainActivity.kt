package nl.rwslinkman.simdeviceble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    private val requestEnableBT = 1337;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val model: AppModel by viewModels()

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        model.bluetoothSupported.postValue(bluetoothAdapter != null)

        bluetoothAdapter?.let {
            model.bluetoothEnabled.postValue(bluetoothAdapter.isEnabled)
            model.bluetoothAdvertisingSupported.postValue(bluetoothAdapter.isMultipleAdvertisementSupported)
            model.advertisementName.postValue(bluetoothAdapter.name)
            model.locationPermissionGranted.postValue( false)

            val advertisementManager = AdvertisementManager(bluetoothAdapter)
        }

        val bluetoothManager : BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager


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

    companion object {
        const val TAG = "MainActivity"
    }
}