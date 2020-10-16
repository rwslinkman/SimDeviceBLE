package nl.rwslinkman.simdeviceble.service.healththermometer

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import nl.rwslinkman.simdeviceble.device.model.Characteristic
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

    override fun validateWrite(): Int {
        // TODO
        return BluetoothGatt.GATT_SUCCESS
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