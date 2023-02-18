package nl.rwslinkman.simdeviceble.service.heartrate

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

/**
 * See [Heart Rate Service](https://www.bluetooth.com/specifications/specs/heart-rate-service-1-0/)
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
        val SERVICE_UUID: UUID = BluetoothUUID.fromSigNumber("180D")
    }
}