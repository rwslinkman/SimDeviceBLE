package nl.rwslinkman.simdeviceble.device.model

import java.util.*

interface Device {
    val name: String
    val primaryServiceUuid: UUID
    val services: List<Service>
}