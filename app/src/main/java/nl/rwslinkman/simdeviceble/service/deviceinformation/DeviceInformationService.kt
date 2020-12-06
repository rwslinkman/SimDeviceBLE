package nl.rwslinkman.simdeviceble.service.deviceinformation

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class DeviceInformationService: Service {
    override val name: String
        get() = "DeviceInformationService"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("180A")
    override val characteristics: List<Characteristic>
        get() = listOf(
            ManufacturerNameCharacteristic(),
            ModelNumberCharacteristic(),
            SerialNumberCharacteristic(),
            HardwareRevisionCharacteristic(),
            FirmwareRevisionCharacteristic(),
            SoftwareRevisionCharacteristic(),
            SystemIdentifierCharacteristic(),
            RegulatoryCertificationCharacteristic(),
            PnpIdentifierCharacteristic()
        )
}