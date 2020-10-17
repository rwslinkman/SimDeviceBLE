package nl.rwslinkman.simdeviceble.service

import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class CurrentTimeService: Service {
    override val name: String
        get() = "CurrentTimeService"

    override val uuid: UUID
        get() = UUID.randomUUID()

    override val characteristics: List<Characteristic>
        get() = listOf()
}