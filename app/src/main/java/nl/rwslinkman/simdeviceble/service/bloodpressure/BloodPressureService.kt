package nl.rwslinkman.simdeviceble.service.bloodpressure

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class BloodPressureService: Service {
    override val name: String
        get() = "BloodPressureService"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("1810")
    override val characteristics: List<Characteristic>
        get() = listOf(
            BloodPressureMeasurementCharacteristic(),
            BloodPressureFeatureCharacteristic()
        )
}