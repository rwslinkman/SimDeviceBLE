package nl.rwslinkman.simdeviceble.service.healththermometer

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothBytesParser
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

/**
 * Temperature Measurement
 */
class TemperatureMeasurementCharacteristic : Characteristic {
    override val name: String
        get() = "TemperatureMeasurement"

    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("2A1C")

    override val type: Characteristic.Type
        get() = Characteristic.Type.Decimal

    override val isRead: Boolean
        get() = true

    override val isNotify: Boolean
        get() = true

    override val isIndicate: Boolean
        get() = true

    override val description: String
        get() = TEMPERATURE_MEASUREMENT_DESCRIPTION

    override val initialValue: ByteArray
        get() = convert(INITIAL_TEMPERATURE_MEASUREMENT_VALUE)

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        return BluetoothGatt.GATT_SUCCESS
    }

    override fun convertToPresentable(value: ByteArray): String {
        val parser = BluetoothBytesParser(value)
        val temperature = parser.getFloatValue(TEMPERATURE_MEASUREMENT_VALUE_FORMAT)
        return temperature.toString()
    }

    override fun convertToBytes(value: String): ByteArray {
        val temperature: Float = value.toFloat()
        return convert(temperature)
    }

    companion object {
        private const val TEMPERATURE_MEASUREMENT_VALUE_FORMAT = BluetoothBytesParser.FORMAT_FLOAT
        private const val INITIAL_TEMPERATURE_MEASUREMENT_VALUE: Float = 37.0f
        private const val EXPONENT_MASK = 0x7f800000
        private const val EXPONENT_SHIFT = 23
        private const val MANTISSA_MASK = 0x007fffff
        private const val MANTISSA_SHIFT = 0
        private const val TEMPERATURE_MEASUREMENT_DESCRIPTION = "This characteristic is used to send a temperature measurement."
    }

    private fun convert(value: Float): ByteArray {
        val parser = BluetoothBytesParser()
        parser.setFloatValue(value, 1)
        return parser.value
    }
}