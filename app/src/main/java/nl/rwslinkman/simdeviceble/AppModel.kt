package nl.rwslinkman.simdeviceble

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.rwslinkman.simdeviceble.device.model.Device

class AppModel: ViewModel() {

    val bluetoothSupported: MutableLiveData<Boolean> = MutableLiveData()
    val bluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val bluetoothAdvertisingSupported: MutableLiveData<Boolean> = MutableLiveData()
    val isAdvertising: MutableLiveData<Boolean> = MutableLiveData()

    val activeDevice: MutableLiveData<Device> = MutableLiveData()

    fun enableBluetooth() {
        // TODO
    }

    fun advertiseDevice(device: Device) {
        // TODO:
    }
}