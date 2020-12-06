package nl.rwslinkman.simdeviceble.device

import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.device.model.Service
import nl.rwslinkman.simdeviceble.service.battery.BatteryService
import nl.rwslinkman.simdeviceble.service.CurrentTimeService
import nl.rwslinkman.simdeviceble.service.deviceinformation.DeviceInformationService
import java.util.*

class DigitalClock: Device() {
    override val name: String
        get() = "Digital Clock"

    override val primaryServiceUuid: UUID
        get() = CurrentTimeService.SERVICE_UUID

    override val services: List<Service>
        get() = listOf(
            BatteryService(),
            CurrentTimeService(),
            DeviceInformationService()
        )
}