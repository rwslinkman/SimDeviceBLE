package nl.rwslinkman.simdeviceble.service.deviceinformation

import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

class PnpIdentifierCharacteristic: Characteristic {
    override val name: String
        get() = "PnpIdentifierCharacteristic"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("2A50")
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