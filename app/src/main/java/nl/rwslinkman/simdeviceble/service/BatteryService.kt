package nl.rwslinkman.simdeviceble.service

import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class BatteryService: Service {
    override val uuid: UUID
        get() = SERVICE_UUID
    override val characteristics: List<Characteristic>
        get() = listOf()

    companion object {
        private val SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb")

        private val BATTERY_LEVEL_UUID = UUID
            .fromString("00002A19-0000-1000-8000-00805f9b34fb")
        private const val INITIAL_BATTERY_LEVEL = 50
        private const val BATTERY_LEVEL_MAX = 100
        private const val BATTERY_LEVEL_DESCRIPTION = "The current charge level of a " +
                "battery. 100% represents fully charged while 0% represents fully discharged."
    }
}