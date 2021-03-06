package nl.rwslinkman.simdeviceble.service.heartrate

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothBytesParser
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

/**
 * Body Sensor Location
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
        return BluetoothGatt.GATT_SUCCESS
    }

    override fun convertToPresentable(value: ByteArray): String {
        val parser = BluetoothBytesParser(value)
        val locationCode = parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8)
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