package nl.rwslinkman.simdeviceble.device

import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.device.model.Service
import nl.rwslinkman.simdeviceble.service.battery.BatteryService
import nl.rwslinkman.simdeviceble.service.deviceinformation.DeviceInformationService
import nl.rwslinkman.simdeviceble.service.environmentalsensing.EnvironmentalSensingService
import java.util.UUID

class RoomSensorDevice: Device() {
    override val name: String
        get() = "Room Sensor"

    override val primaryServiceUuid: UUID
        get() = EnvironmentalSensingService.SERVICE_UUID

    override val services: List<Service>
        get() = listOf(
            EnvironmentalSensingService(),
            BatteryService(),
            DeviceInformationService()
        )
}