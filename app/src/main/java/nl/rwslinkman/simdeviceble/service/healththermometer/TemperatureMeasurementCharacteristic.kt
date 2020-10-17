package nl.rwslinkman.simdeviceble.service.healththermometer

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

/**
 * See [Temperature Measurement](https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.temperature_measurement.xml)
 */
class TemperatureMeasurementCharacteristic : Characteristic {
    override val name: String
        get() = "TemperatureMeasurement"

    override val uuid: UUID
        get() = UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb")

    override val isIndicate: Boolean
        get() = true

    override val description: String?
        get() = TEMPERATURE_MEASUREMENT_DESCRIPTION

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        // TODO
        return BluetoothGatt.GATT_SUCCESS
    }

    companion object {
        private val TEMPERATURE_MEASUREMENT_VALUE_FORMAT = BluetoothGattCharacteristic.FORMAT_FLOAT
        private val INITIAL_TEMPERATURE_MEASUREMENT_VALUE = 37.0f
        private val EXPONENT_MASK = 0x7f800000
        private val EXPONENT_SHIFT = 23
        private val MANTISSA_MASK = 0x007fffff
        private val MANTISSA_SHIFT = 0
        private val TEMPERATURE_MEASUREMENT_DESCRIPTION = "This characteristic is used " +
                    "to send a temperature measurement."
    }
}