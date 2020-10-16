package nl.rwslinkman.simdeviceble.device

import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.device.model.Service
import nl.rwslinkman.simdeviceble.service.battery.BatteryService
import nl.rwslinkman.simdeviceble.service.healththermometer.HealthThermometerService
import java.util.*

class EarThermometer: Device() {

    override val name: String
        get() = "Thermometer (Ear)"

    override val primaryServiceUuid: UUID
        get() = HealthThermometerService.SERVICE_UUID

    override val services: List<Service>
        get() = listOf(
            HealthThermometerService(),
            BatteryService()
        )
}