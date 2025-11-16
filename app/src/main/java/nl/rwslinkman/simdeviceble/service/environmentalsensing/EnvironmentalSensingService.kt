package nl.rwslinkman.simdeviceble.service.environmentalsensing

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.UUID

class EnvironmentalSensingService: Service {
    override val name: String
        get() = "EnvironmentalSensingService"
    override val uuid: UUID
        get() = SERVICE_UUID
    override val characteristics: List<Characteristic>
        get() = listOf(
            WindChillCharacteristic()
        )

    companion object {
        val SERVICE_UUID: UUID = BluetoothUUID.fromSigNumber("181A")
    }
}