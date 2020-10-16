package nl.rwslinkman.simdeviceble.device.heartrate

import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.device.model.Service
import nl.rwslinkman.simdeviceble.service.battery.BatteryService
import nl.rwslinkman.simdeviceble.service.heartrate.HeartRateService
import java.util.*

class HeartRatePeripheral: Device() {

    override val name: String
        get() = "Fitness Band (Chest)"

    override val primaryServiceUuid: UUID
        get() = HeartRateService.SERVICE_UUID

    override val services: List<Service>
        get() = listOf(
            HeartRateService(),
            BatteryService()
        )
}