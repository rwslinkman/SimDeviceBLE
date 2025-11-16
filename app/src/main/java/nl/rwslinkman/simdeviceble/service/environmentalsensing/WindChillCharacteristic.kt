package nl.rwslinkman.simdeviceble.service.environmentalsensing

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.UUID

class WindChillCharacteristic: Characteristic {
    override val name: String
        get() = "WindChillCharacteristic"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("2A79")
    override val type: Characteristic.Type
        get() = Characteristic.Type.Number

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