package nl.rwslinkman.simdeviceble

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.rwslinkman.simdeviceble.device.heartrate.HeartRatePeripheral
import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.device.time.Clock

class AppModel: ViewModel() {

    val bluetoothSupported: MutableLiveData<Boolean> = MutableLiveData(false)
    val bluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val bluetoothAdvertisingSupported: MutableLiveData<Boolean> = MutableLiveData(false)
    val locationPermissionGranted: MutableLiveData<Boolean> = MutableLiveData(false)

    val isAdvertising: MutableLiveData<Boolean> = MutableLiveData(false)
    val advertisementName: MutableLiveData<String> = MutableLiveData("Unknown")
    val activeDevice: MutableLiveData<Device> = MutableLiveData(supportedDevices.first())
    val isConnectable: MutableLiveData<Boolean> = MutableLiveData(true)

    fun enableBluetooth() {
        // TODO
    }

    fun startPermissionFlow() {
        // TODO
    }

    fun selectDevice(device: Device) {
        activeDevice.postValue(device)
    }

    fun startAdvertising() {

    }

    fun stopAdvertising() {
        // TODO
    }

    companion object {
        val supportedDevices: List<Device> = listOf(
            HeartRatePeripheral(),
            Clock()
        )
    }
}