package nl.rwslinkman.simdeviceble.service.weightscale

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class WeightScaleService: Service {
    override val name: String
        get() = "WeightScaleService"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("181D")
    override val characteristics: List<Characteristic>
        get() = listOf() // TODO
}