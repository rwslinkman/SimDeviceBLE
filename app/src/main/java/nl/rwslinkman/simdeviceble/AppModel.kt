package nl.rwslinkman.simdeviceble

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.rwslinkman.simdeviceble.device.Peripheral

class AppModel: ViewModel() {

    val bluetoothSupported: MutableLiveData<Boolean> = MutableLiveData()
    val bluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val bluetoothAdvertisingSupported: MutableLiveData<Boolean> = MutableLiveData()
    val isAdvertising: MutableLiveData<Boolean> = MutableLiveData()

    val activeDevice: Peripheral? = null

    fun enableBluetooth() {
        // TODO
    }
}