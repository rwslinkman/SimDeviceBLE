package nl.rwslinkman.simdeviceble.service.healththermometer

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import kotlin.math.pow


/**
 * See [Measurement Interval](https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.measurement_interval.xml)
 */
class MeasurementIntervalCharacteristic: Characteristic {
    override val name: String
        get() = "MeasurementInterval"

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

    override val initialValue: ByteArray?
        get() = convertToBytes(INITIAL_MEASUREMENT_INTERVAL.toString())

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

    override fun convertToPresentable(value: ByteArray): String {
        val buffer = ByteBuffer.allocate(Int.SIZE_BYTES)
        buffer.put(value)
        buffer.flip()
        val interval = buffer.int
        return "$interval second(s)"
    }

    override fun convertToBytes(value: String): ByteArray {
        val newLevel = Integer.parseInt(value)
        val buffer: ByteBuffer = ByteBuffer.allocate(Int.SIZE_BYTES)
        buffer.putInt(newLevel)
        return buffer.array()
    }

    private fun isValueWithinLimits(value: Short): Boolean {
        return (value >= MIN_MEASUREMENT_INTERVAL) && (value <= MAX_MEASUREMENT_INTERVAL)
    }

    companion object {
        private const val INITIAL_MEASUREMENT_INTERVAL = 1
        private const val MIN_MEASUREMENT_INTERVAL = 1
        private val MAX_MEASUREMENT_INTERVAL = 2.0.pow(16.0).toInt() - 1
        private const val MEASUREMENT_INTERVAL_DESCRIPTION = "This characteristic is used " +
                "to enable and control the interval between consecutive temperature measurements."
    }
}