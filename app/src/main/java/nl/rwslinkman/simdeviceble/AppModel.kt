package nl.rwslinkman.simdeviceble

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.rwslinkman.simdeviceble.SimDevices.supportedDevices
import nl.rwslinkman.simdeviceble.bluetooth.AdvertisementManager
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothDelegate
import nl.rwslinkman.simdeviceble.device.DigitalClock
import nl.rwslinkman.simdeviceble.device.EarThermometer
import nl.rwslinkman.simdeviceble.device.HeartRatePeripheral
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Device
import java.util.*

class AppModel: ViewModel(), AdvertisementManager.Listener {

    // Data for Bluetooth UI
    val bluetoothSupported: MutableLiveData<Boolean> = MutableLiveData(false)
    val bluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val bluetoothAdvertisingSupported: MutableLiveData<Boolean> = MutableLiveData(false)
    // Data for Advertising UI
    val isAdvertising: MutableLiveData<Boolean> = MutableLiveData(false)
    val advertisementName: MutableLiveData<String> = MutableLiveData("Unknown")
    val activeDevice: MutableLiveData<Device> = MutableLiveData(supportedDevices.first())
    val isConnectable: MutableLiveData<Boolean> = MutableLiveData(true)
    val advertiseDeviceName: MutableLiveData<Boolean> = MutableLiveData(true)
    // Data for Connections UI
    private val _connDevices: MutableSet<String> = mutableSetOf()
    val connectedDevices: MutableLiveData<List<String>> = MutableLiveData()
    // Characteristic data manipulation
    private val _dataContainer: MutableMap<UUID, ByteArray> = mutableMapOf()
    val presentableDataContainer: MutableLiveData<Map<UUID, String>> = MutableLiveData(mutableMapOf())

    // Handle to Activity for BLE related operations
    val bluetoothDelegate: MutableLiveData<BluetoothDelegate> = MutableLiveData()

    fun enableBluetooth() {
        bluetoothDelegate.value?.turnOnBluetooth()
    }

    fun selectDevice(device: Device) {
        activeDevice.postValue(device)
    }

    fun startAdvertising() {
        activeDevice.value?.let {
            val allowDeviceName: Boolean = if (advertiseDeviceName.value == null) defaultAllowDeviceName else advertiseDeviceName.value!!
            val isConnectable: Boolean = if (isConnectable.value == null) defaultIsConnectable else isConnectable.value!!
            bluetoothDelegate.value?.advertise(it, allowDeviceName, isConnectable)
        }
    }

    fun stopAdvertising() {
        bluetoothDelegate.value?.stopAdvertising()
        _dataContainer.clear()
    }

    override fun onDeviceConnected(deviceAddress: String) {
        _connDevices.add(deviceAddress)
        connectedDevices.postValue(_connDevices.toList())
    }

    override fun onDeviceDisconnected(deviceAddress: String) {
        _connDevices.remove(deviceAddress)
        connectedDevices.postValue(_connDevices.toList())
    }

    override fun updateDataContainer(characteristic: Characteristic, data: ByteArray, isInitialValue: Boolean) {
        _dataContainer[characteristic.uuid] = data
        postDataContainer()
    }

    override fun setIsAdvertising(isAdvertising: Boolean, advertisedDevice: String?) {
        this.isAdvertising.postValue(isAdvertising)
    }

    fun updateCharacteristicValue(characteristic: Characteristic, value: String) {
        val byteValue: ByteArray = characteristic.convertToBytes(value)
        updateDataContainer(characteristic, byteValue, false)
    }

    fun sendCharacteristicNotification(characteristic: Characteristic) {
        bluetoothDelegate.value?.sendNotificationToConnectedDevices(characteristic)
    }

    private fun postDataContainer() {
        bluetoothDelegate.value?.updateCharacteristicValues(_dataContainer)

        // Convert to data for Fragments
        val presentable = mutableMapOf<UUID, String>()
        _dataContainer.forEach {
            val char = activeDevice.value!!.getCharacteristic(it.key)
            presentable[it.key] = char?.convertToPresentable(it.value) ?: ""
        }
        presentableDataContainer.postValue(presentable)
    }

    companion object {
        const val sourcesLink: String = "https://github.com/rwslinkman/simdeviceble"
        const val developerLink: String = "https://rwslinkman.nl"
        const val defaultAllowDeviceName: Boolean = true
        const val defaultIsConnectable: Boolean = true
    }
}