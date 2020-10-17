package nl.rwslinkman.simdeviceble.service.battery

import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class BatteryService: Service {
    override val name: String
        get() = "BatteryService"

    override val uuid: UUID
        get() = SERVICE_UUID

    override val characteristics: List<Characteristic>
        get() = listOf(
            BatteryLevelCharacteristic()
        )

    companion object {
        private val SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb")

        private const val INITIAL_BATTERY_LEVEL = 50
        private const val BATTERY_LEVEL_MAX = 100
    }
}