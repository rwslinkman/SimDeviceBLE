package nl.rwslinkman.simdeviceble.service.heartrate

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothBytesParser
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

/**
 * Heart Rate Control Point
 */
class HeartRateControlPointCharacteristic: Characteristic {
    override val name: String
        get() = "HeartRateControlPoint"

    override val uuid: UUID
        get() = CHAR_UUID

    override val type: Characteristic.Type
        get() = Characteristic.Type.Number

    override val isWrite: Boolean
        get() = true

    override val initialValue: ByteArray?
        get() = convert(INITIAL_EXPENDED_ENERGY)

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        return BluetoothGatt.GATT_SUCCESS
    }

    override fun convertToPresentable(value: ByteArray): String {
        val parser = BluetoothBytesParser(value)
        val controlPoint: Int = parser.getIntValue(EXPENDED_ENERGY_FORMAT)
        return controlPoint.toString()
    }

    override fun convertToBytes(value: String): ByteArray {
        val controlPoint = Integer.parseInt(value)
        return convert(controlPoint)
    }

    private fun convert(controlPoint: Int): ByteArray {
        val parser = BluetoothBytesParser()
        parser.setIntValue(controlPoint, EXPENDED_ENERGY_FORMAT)
        return parser.value
    }

    companion object {
        val CHAR_UUID: UUID = UUID.fromString("00002A39-0000-1000-8000-00805f9b34fb")
        private const val EXPENDED_ENERGY_FORMAT = BluetoothGattCharacteristic.FORMAT_UINT16
        private const val INITIAL_EXPENDED_ENERGY = 0
    }
}