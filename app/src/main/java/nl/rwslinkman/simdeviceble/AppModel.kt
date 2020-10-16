package nl.rwslinkman.simdeviceble

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothDelegate
import nl.rwslinkman.simdeviceble.device.heartrate.HeartRatePeripheral
import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.device.time.Clock

class AppModel: ViewModel() {

    val bluetoothSupported: MutableLiveData<Boolean> = MutableLiveData(false)
    val bluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val bluetoothAdvertisingSupported: MutableLiveData<Boolean> = MutableLiveData(false)

    val isAdvertising: MutableLiveData<Boolean> = MutableLiveData(false)
    val advertisementName: MutableLiveData<String> = MutableLiveData("Unknown")
    val activeDevice: MutableLiveData<Device> = MutableLiveData(supportedDevices.first())
    val isConnectable: MutableLiveData<Boolean> = MutableLiveData(true)
    val advertiseDeviceName: MutableLiveData<Boolean> = MutableLiveData(true)

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

    companion object {
        val supportedDevices: List<Device> = listOf(
            HeartRatePeripheral(),
            Clock()
        )
        const val defaultAllowDeviceName: Boolean = true
        const val defaultIsConnectable: Boolean = true
    }
}