package nl.rwslinkman.simdeviceble.service.healththermometer

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

/**
 * See [Measurement Interval](https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.measurement_interval.xml)
 */
class MeasurementIntervalCharacteristic: Characteristic {

    override val uuid: UUID
        get() = UUID.fromString("00002A21-0000-1000-8000-00805f9b34fb")

    override val isRead: Boolean
        get() = true

    override val isWrite: Boolean
        get() = true

    override val isIndicate: Boolean
        get() = true

    override val description: String?
        get() = MEASUREMENT_INTERVAL_DESCRIPTION

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        if (offset != 0) {
            return BluetoothGatt.GATT_INVALID_OFFSET
        }

        value?.let {
            if(it.size != 2) {
                return BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH
            }
            // Parse byte to numeric value
            val byteBuffer: ByteBuffer = ByteBuffer.wrap(value)
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val newMeasurementIntervalValue: Short = byteBuffer.short

            // Check value limits and return
            return if (!isValueWithinLimits(newMeasurementIntervalValue)) {
                BluetoothGatt.GATT_FAILURE
            } else BluetoothGatt.GATT_SUCCESS
        } ?: return BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH
    }

    private fun isValueWithinLimits(value: Short): Boolean {
        return (value >= MIN_MEASUREMENT_INTERVAL) && (value <= MAX_MEASUREMENT_INTERVAL);
    }

    companion object {
        private val MEASUREMENT_INTERVAL_FORMAT = BluetoothGattCharacteristic.FORMAT_UINT16
        private val INITIAL_MEASUREMENT_INTERVAL = 1
        private val MIN_MEASUREMENT_INTERVAL = 1
        private val MAX_MEASUREMENT_INTERVAL = Math.pow(2.0, 16.0).toInt() - 1
        private val MEASUREMENT_INTERVAL_DESCRIPTION = "This characteristic is used " +
                "to enable and control the interval between consecutive temperature measurements."
    }
}