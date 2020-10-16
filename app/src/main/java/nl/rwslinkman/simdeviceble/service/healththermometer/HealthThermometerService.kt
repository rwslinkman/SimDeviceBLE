package nl.rwslinkman.simdeviceble.service.healththermometer

import android.bluetooth.BluetoothGattCharacteristic
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

/**
 * See [Health Thermometer Service](https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.health_thermometer.xml)
 * This service exposes two characteristics with descriptors:
 * - Measurement Interval Characteristic:
 * - Listen to notifications to from which you can subscribe to notifications
 * - CCCD Descriptor:
 * - Read/Write to get/set notifications.
 * - User Description Descriptor:
 * - Read/Write to get/set the description of the Characteristic.
 * - Temperature Measurement Characteristic:
 * - Read value to get the current interval of the temperature measurement timer.
 * - Write value resets the temperature measurement timer with the new value. This timer
 * is responsible for triggering value changed events every "Measurement Interval" value.
 * - CCCD Descriptor:
 * - Read/Write to get/set notifications.
 * - User Description Descriptor:
 * - Read/Write to get/set the description of the Characteristic.
 */
class HealthThermometerService: Service {
    override val uuid: UUID
        get() = SERVICE_UUID

    override val characteristics: List<Characteristic>
        get() = listOf(
            TemperatureMeasurementCharacteristic(),
            MeasurementIntervalCharacteristic()
        )

    companion object {
        val SERVICE_UUID = UUID.fromString("00001809-0000-1000-8000-00805f9b34fb")
    }
}