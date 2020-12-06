package nl.rwslinkman.simdeviceble.service.heartrate

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothBytesParser
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

/**
 * Heart Rate Measurement
 */
class HeartRateMeasurementCharacteristic: Characteristic {
    override val name: String
        get() = "HeartRateMeasurement"

    override val uuid: UUID
        get() = CHAR_UUID

    override val type: Characteristic.Type
        get() = Characteristic.Type.Number

    override val isRead: Boolean
        get() = true

    override val isNotify: Boolean
        get() = true

    override val description: String?
        get() = HEART_RATE_MEASUREMENT_DESCRIPTION

    override val initialValue: ByteArray?
        get() = convert(INITIAL_HEART_RATE_MEASUREMENT_VALUE)

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        return BluetoothGatt.GATT_SUCCESS
    }

    override fun convertToPresentable(value: ByteArray): String {
        val parser = BluetoothBytesParser(value)
        val heartRate: Int = parser.getIntValue(HEART_RATE_MEASUREMENT_VALUE_FORMAT)
        return heartRate.toString()
    }

    override fun convertToBytes(value: String): ByteArray {
        val heartRate = Integer.parseInt(value)
        return convert(heartRate)
    }

    companion object {
        val CHAR_UUID: UUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")
        private const val HEART_RATE_MEASUREMENT_VALUE_FORMAT = BluetoothBytesParser.FORMAT_UINT8
        private const val INITIAL_HEART_RATE_MEASUREMENT_VALUE = 60
        private const val HEART_RATE_MEASUREMENT_DESCRIPTION = "Used to send a heart rate measurement"
    }

    private fun convert(value: Int): ByteArray {
        val parser = BluetoothBytesParser()
        parser.setIntValue(value, HEART_RATE_MEASUREMENT_VALUE_FORMAT)
        return parser.value
    }
}