package nl.rwslinkman.simdeviceble.service

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class CurrentTimeService: Service {
    override val name: String
        get() = "CurrentTimeService"

    override val uuid: UUID
        get() = SERVICE_UUID

    override val characteristics: List<Characteristic>
        get() = listOf()

    companion object {
        val SERVICE_UUID = BluetoothUUID.fromSigNumber("1805")
    }
}