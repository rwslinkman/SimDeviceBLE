package nl.rwslinkman.simdeviceble.service.deviceinformation

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

class HardwareRevisionCharacteristic: Characteristic {
    override val name: String
        get() = "HardwareRevisionCharacteristic"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("2A27")
    override val isRead: Boolean
        get() = true

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        TODO("Not yet implemented")
    }

    override fun convertToPresentable(value: ByteArray): String {
        TODO("Not yet implemented")
    }

    override fun convertToBytes(value: String): ByteArray {
        TODO("Not yet implemented")
    }
}