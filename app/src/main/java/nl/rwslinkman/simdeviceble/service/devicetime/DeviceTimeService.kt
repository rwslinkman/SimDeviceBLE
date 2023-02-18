package nl.rwslinkman.simdeviceble.service.devicetime

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class DeviceTimeService: Service {
    override val name: String
        get() = "DeviceTimeService"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("1847")
    override val characteristics: List<Characteristic>
        get() = listOf(
            DeviceTimeFeatureCharacteristic(),
            DeviceTimeParametersCharacteristic(),
            DeviceTimeCharacteristic(),
            DeviceTimeControlPointCharacteristic(),
        )
}