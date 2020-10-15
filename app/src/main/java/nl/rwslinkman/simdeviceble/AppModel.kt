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

    val isAdvertising: MutableLiveData<Boolean> = MutableLiveData(false)
    val advertisementName: MutableLiveData<String> = MutableLiveData("Unknown")
    val activeDevice: MutableLiveData<Device> = MutableLiveData(supportedDevices.first())
    val isConnectable: MutableLiveData<Boolean> = MutableLiveData(true)
    val advertiseDeviceName: MutableLiveData<Boolean> = MutableLiveData(true)

    var advertisementManager: AdvertisementManager? = null

    fun enableBluetooth() {
        advertisementManager?.turnOnBluetooth()
    }

    fun selectDevice(device: Device) {
        activeDevice.postValue(device)
    }

    fun startAdvertising() {
        activeDevice.value?.let {
            val allowDeviceName: Boolean = if (advertiseDeviceName.value == null) true else advertiseDeviceName.value!!
            advertisementManager?.advertise(it, allowDeviceName)
        }
    }

    fun stopAdvertising() {
        advertisementManager?.stop()
    }

    companion object {
        val supportedDevices: List<Device> = listOf(
            HeartRatePeripheral(),
            Clock()
        )
    }
}