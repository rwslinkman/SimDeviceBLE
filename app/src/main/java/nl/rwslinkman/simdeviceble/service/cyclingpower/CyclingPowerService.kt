package nl.rwslinkman.simdeviceble.service.cyclingpower

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class CyclingPowerService: Service {
    override val name: String
        get() = "CyclingPowerService"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("1818")
    override val characteristics: List<Characteristic>
        get() = listOf() // TODO
}