package nl.rwslinkman.simdeviceble.device.model

import java.util.*

interface Service {
    val uuid: UUID
    val characteristics: List<Characteristic>
}