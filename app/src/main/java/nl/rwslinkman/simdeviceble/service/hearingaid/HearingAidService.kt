package nl.rwslinkman.simdeviceble.service.hearingaid

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class HearingAidService: Service {
    override val name: String
        get() = "HearingAidService"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("1854")
    override val characteristics: List<Characteristic>
        get() = listOf() // TODO
}