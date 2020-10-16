package nl.rwslinkman.simdeviceble

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothDelegate
import nl.rwslinkman.simdeviceble.device.HeartRatePeripheral
import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.device.Clock
import nl.rwslinkman.simdeviceble.device.EarThermometer

class AppModel: ViewModel() {

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
    }

    fun onDeviceConnected(device: String) {
        _connDevices.add(device)
        connectedDevices.postValue(_connDevices.toList())
    }

    fun onDeviceDisconnected(device: String) {
        _connDevices.remove(device)
        connectedDevices.postValue(_connDevices.toList())
    }

    companion object {
        val supportedDevices: List<Device> = listOf(
            HeartRatePeripheral(),
            Clock(),
            EarThermometer()
        )
        const val defaultAllowDeviceName: Boolean = true
        const val defaultIsConnectable: Boolean = true
    }
}