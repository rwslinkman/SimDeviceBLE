package nl.rwslinkman.simdeviceble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import nl.rwslinkman.simdeviceble.bluetooth.AdvertiseCommand
import nl.rwslinkman.simdeviceble.bluetooth.AdvertisementManager
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothDelegate
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.grpc.GrpcServerActivity
import java.util.*

@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {

    private val appModel: AppModel by viewModels()
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var advManager: AdvertisementManager? = null

    private val btDelegate = object :
        BluetoothDelegate {
        override fun turnOnBluetooth() {
            startBluetoothIntent()
        }

        override fun advertise(device: Device, includeDeviceName: Boolean, isConnectable: Boolean) {
            val command = AdvertiseCommand(device, includeDeviceName, isConnectable)
            advManager?.advertise(command)
        }

        override fun stopAdvertising() {
            advManager?.stop()
        }

        override fun updateCharacteristicValues(characteristicData: MutableMap<UUID, ByteArray>) {
            advManager?.updateAdvertisedCharacteristics(characteristicData)
        }

        override fun sendNotificationToConnectedDevices(characteristic: Characteristic) {
            advManager?.sendNotificationToConnectedDevices(characteristic)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBluetooth()
        setupNavigation()
        appModel.bluetoothDelegate.postValue(btDelegate)
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
        advManager?.stop()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.header_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_item_grpc) {
            val intent = Intent(this, GrpcServerActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startBluetoothIntent() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_supported_devices,
                R.id.navigation_servicedata
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        appModel.bluetoothSupported.postValue(bluetoothAdapter != null)

        bluetoothAdapter?.let {
            advManager = AdvertisementManager(this, it, appModel)

            appModel.bluetoothEnabled.postValue(it.isEnabled)
            appModel.bluetoothAdvertisingSupported.postValue(it.isMultipleAdvertisementSupported)
//            appModel.advertisementName.postValue(it.name)
        }
    }

    companion object {
        const val REQUEST_ENABLE_BT = 1337
    }
}