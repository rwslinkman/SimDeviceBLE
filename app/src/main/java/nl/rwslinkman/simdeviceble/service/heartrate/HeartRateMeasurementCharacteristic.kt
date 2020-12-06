package nl.rwslinkman.simdeviceble.service.heartrate

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.text.Editable
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

    override val isNotify: Boolean
        get() = true

    override val description: String?
        get() = HEART_RATE_MEASUREMENT_DESCRIPTION

    override val initialValue: ByteArray?
        get() = INITIAL_HEART_RATE_MEASUREMENT_VALUE.toString().toByteArray()

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        // TODO
        return BluetoothGatt.GATT_SUCCESS
    }

    override fun convertToPresentable(value: ByteArray): String {
        // TODO
        return value.first().toInt().toString()
    }

    override fun convertToBytes(value: String): ByteArray {
        return value.toByteArray()
    }

    companion object {
        val CHAR_UUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")
        private val HEART_RATE_MEASUREMENT_VALUE_FORMAT = BluetoothGattCharacteristic.FORMAT_UINT8
        private val INITIAL_HEART_RATE_MEASUREMENT_VALUE = 60
        private val EXPENDED_ENERGY_FORMAT = BluetoothGattCharacteristic.FORMAT_UINT16
        private val INITIAL_EXPENDED_ENERGY = 0
        private val HEART_RATE_MEASUREMENT_DESCRIPTION = "Used to send a heart rate measurement"
    }
}