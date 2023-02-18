package nl.rwslinkman.simdeviceble.service.fitnessmachine

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class FitnessMachineService: Service {
    override val name: String
        get() = "FitnessMachineService"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("1826")
    override val characteristics: List<Characteristic>
        get() = listOf() // TODO
}