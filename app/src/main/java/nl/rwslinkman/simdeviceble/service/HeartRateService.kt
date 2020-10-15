package nl.rwslinkman.simdeviceble.service

import android.bluetooth.BluetoothGattCharacteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*


class HeartRateService: Service {
    private val MIN_UINT = 0
    private val MAX_UINT8 = Math.pow(2.0, 8.0).toInt() - 1
    private val MAX_UINT16 = Math.pow(2.0, 16.0).toInt() - 1

    /**
     * See [
 * Heart Rate Service](https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.heart_rate.xml)
     */
    private val HEART_RATE_SERVICE_UUID: UUID = UUID
        .fromString("0000180D-0000-1000-8000-00805f9b34fb")

    /**
     * See [
 * Heart Rate Measurement](https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml)
     */
    private val HEART_RATE_MEASUREMENT_UUID: UUID = UUID
        .fromString("00002A37-0000-1000-8000-00805f9b34fb")
    private val HEART_RATE_MEASUREMENT_VALUE_FORMAT = BluetoothGattCharacteristic.FORMAT_UINT8
    private val INITIAL_HEART_RATE_MEASUREMENT_VALUE = 60
    private val EXPENDED_ENERGY_FORMAT = BluetoothGattCharacteristic.FORMAT_UINT16
    private val INITIAL_EXPENDED_ENERGY = 0
    private val HEART_RATE_MEASUREMENT_DESCRIPTION = "Used to send a heart rate " +
            "measurement"

    /**
     * See [
 * Body Sensor Location](https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.body_sensor_location.xml)
     */
    private val BODY_SENSOR_LOCATION_UUID: UUID = UUID
        .fromString("00002A38-0000-1000-8000-00805f9b34fb")
    private val LOCATION_OTHER = 0

    /**
     * See [
 * Heart Rate Control Point](https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_control_point.xml)
     */
    private val HEART_RATE_CONTROL_POINT_UUID: UUID = UUID
        .fromString("00002A39-0000-1000-8000-00805f9b34fb")

}