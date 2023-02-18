package nl.rwslinkman.simdeviceble.service.pulseoxi

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class PulseOximeterService: Service {
    override val name: String
        get() = "PulseOximeterService"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("1822")
    override val characteristics: List<Characteristic>
        get() = listOf() // TODO
}