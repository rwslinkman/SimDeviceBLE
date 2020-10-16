package nl.rwslinkman.simdeviceble.service.heartrate

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

/**
 * See [Heart Rate Measurement](https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml)
 */
class HeartRateMeasurementCharacteristic: Characteristic {


    private val HEART_RATE_MEASUREMENT_VALUE_FORMAT = BluetoothGattCharacteristic.FORMAT_UINT8
    private val INITIAL_HEART_RATE_MEASUREMENT_VALUE = 60
    private val EXPENDED_ENERGY_FORMAT = BluetoothGattCharacteristic.FORMAT_UINT16
    private val INITIAL_EXPENDED_ENERGY = 0
    private val HEART_RATE_MEASUREMENT_DESCRIPTION = "Used to send a heart rate " +
            "measurement"

    override val uuid: UUID
        get() = CHAR_UUID

    override val isNotify: Boolean
        get() = true

    override fun validateWrite(): Int {
        // TODO
        return BluetoothGatt.GATT_SUCCESS
    }

    companion object {
        val CHAR_UUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")

    }
}