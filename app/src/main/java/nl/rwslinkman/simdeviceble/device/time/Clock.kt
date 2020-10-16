package nl.rwslinkman.simdeviceble.device.time

import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.device.model.Service
import nl.rwslinkman.simdeviceble.service.battery.BatteryService
import nl.rwslinkman.simdeviceble.service.CurrentTimeService
import java.util.*

class Clock: Device() {
    override val name: String
        get() = "Digital clock"
    override val primaryServiceUuid: UUID
        get() = UUID.randomUUID()
    override val services: List<Service>
        get() = listOf(
            BatteryService(),
            CurrentTimeService()
        )
}