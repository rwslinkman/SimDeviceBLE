package nl.rwslinkman.simdeviceble.service.deviceinformation

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

class SerialNumberCharacteristic: Characteristic {
    override val name: String
        get() = "SerialNumberCharacteristic"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("2A25")
    override val type: Characteristic.Type
        get() = Characteristic.Type.Text
    override val isRead: Boolean
        get() = true

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        return BluetoothGatt.GATT_SUCCESS
    }

    override fun convertToPresentable(value: ByteArray): String {
        return String(value)
    }

    override fun convertToBytes(value: String): ByteArray {
        return value.toByteArray()
    }
}