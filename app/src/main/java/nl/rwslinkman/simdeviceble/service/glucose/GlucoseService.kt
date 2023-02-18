package nl.rwslinkman.simdeviceble.service.glucose

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class GlucoseService: Service {
    override val name: String
        get() = "GlucoseService"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("1808")
    override val characteristics: List<Characteristic>
        get() = listOf() // TODO
}