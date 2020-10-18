package nl.rwslinkman.simdeviceble.service.heartrate

import android.bluetooth.BluetoothGatt
import android.text.Editable
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

/**
 * See [Body Sensor Location](https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.body_sensor_location.xml)
 */
class BodySensorLocationCharacteristic: Characteristic {
    override val name: String
        get() = "BodySensorLocation"

    override val uuid: UUID
        get() = CHAR_UUID

    override val isRead: Boolean
        get() = true

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        // TODO
        return BluetoothGatt.GATT_SUCCESS
    }

    override fun convertToPresentable(value: ByteArray): String {
        // TODO
        return value.first().toInt().toString()
    }

    override fun convertToBytes(value: Editable): ByteArray {
        // TODO
        return ByteArray(1)
    }

    companion object {
        val CHAR_UUID = UUID.fromString("00002A38-0000-1000-8000-00805f9b34fb")
    }
}