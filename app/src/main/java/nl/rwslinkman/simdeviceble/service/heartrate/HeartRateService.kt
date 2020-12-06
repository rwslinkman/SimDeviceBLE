package nl.rwslinkman.simdeviceble.service.heartrate

import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

/**
 * See [
 * Heart Rate Service](https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.heart_rate.xml)
 */
class HeartRateService: Service {
    override val name: String
        get() = "HeartRateService"

    override val uuid: UUID
        get() = SERVICE_UUID

    override val characteristics: List<Characteristic>
        get() = listOf(
            HeartRateMeasurementCharacteristic(),
            BodySensorLocationCharacteristic(),
            HeartRateControlPointCharacteristic()
        )

    companion object {
        val SERVICE_UUID: UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
    }
}