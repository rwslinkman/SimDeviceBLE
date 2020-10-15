package nl.rwslinkman.simdeviceble

import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import nl.rwslinkman.simdeviceble.device.model.Device


class AdvertisementManager(
    private val context: MainActivity,
    bluetoothAdapter: BluetoothAdapter,
    private val appModel: AppModel
) {

    private val bluetoothManager : BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val advertiser = bluetoothAdapter.bluetoothLeAdvertiser
    private var gattServer: BluetoothGattServer? = null

    val gattCallback = object : BluetoothGattServerCallback() {
        // TODO
    }
    val scanCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.d(TAG, "onStartSuccess: ")
            appModel.isAdvertising.postValue(true)
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.d(TAG, "onStartFailure: ")
        }
    }

    fun advertise(device: Device, includeDeviceName: Boolean) {
        gattServer = bluetoothManager.openGattServer(context, gattCallback)

        // TODO: add service

        val mAdvSettings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .setConnectable(true)
            .build()
        val mAdvData = AdvertiseData.Builder()
            .setIncludeTxPowerLevel(true)
            .addServiceUuid(ParcelUuid(device.primaryServiceUuid))
            .build()

        val mAdvScanResponse = AdvertiseData.Builder()
            .setIncludeDeviceName(includeDeviceName)
            .build()
        advertiser.startAdvertising(mAdvSettings, mAdvData, mAdvScanResponse, scanCallback)
    }

    fun stop() {
        gattServer?.clearServices()
        gattServer?.close()
        gattServer = null
        
        advertiser?.stopAdvertising(scanCallback)
        appModel.isAdvertising.postValue(false)
    }

    fun turnOnBluetooth() {
        context.startBluetoothIntent()
    }

    companion object {
        const val TAG = "AdvertisementManager"
    }
}