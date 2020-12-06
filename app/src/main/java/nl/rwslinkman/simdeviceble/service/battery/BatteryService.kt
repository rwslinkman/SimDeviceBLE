package nl.rwslinkman.simdeviceble.service.battery

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
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
        private val SERVICE_UUID = BluetoothUUID.fromSigNumber("180F")
    }
}