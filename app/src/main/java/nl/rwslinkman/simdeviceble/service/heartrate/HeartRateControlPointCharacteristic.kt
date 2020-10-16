package nl.rwslinkman.simdeviceble.service.heartrate

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

/**
 * See [Heart Rate Control Point](https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_control_point.xml)
 */
class HeartRateControlPointCharacteristic: Characteristic {

    override val uuid: UUID
        get() = CHAR_UUID

    override val isWrite: Boolean
        get() = true

    override fun validateWrite(): Int {
        // TODO
        return BluetoothGatt.GATT_SUCCESS
    }

    companion object {
        val CHAR_UUID = UUID.fromString("00002A39-0000-1000-8000-00805f9b34fb")
    }
}