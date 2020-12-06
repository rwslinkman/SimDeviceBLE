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

    override val type: Characteristic.Type
        get() = Characteristic.Type.Number

    override val isRead: Boolean
        get() = true

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        // TODO
        return BluetoothGatt.GATT_SUCCESS
    }

    override fun convertToPresentable(value: ByteArray): String {
        val locationCode = value.first().toInt()
        val locationName = when (locationCode) {
            1 -> "Chest"
            2 -> "Wrist"
            3 -> "Finger"
            4 -> "Hand"
            5 -> "Ear lobe"
            6 -> "Foot"
            else -> "Other"
        }
        return "$locationName ($locationCode)"
    }

    override fun convertToBytes(value: String): ByteArray {
        val retVal = ByteArray(1)
        retVal[0] = value.toByte()
        return retVal
    }

    companion object {
        val CHAR_UUID: UUID = UUID.fromString("00002A38-0000-1000-8000-00805f9b34fb")
    }
}