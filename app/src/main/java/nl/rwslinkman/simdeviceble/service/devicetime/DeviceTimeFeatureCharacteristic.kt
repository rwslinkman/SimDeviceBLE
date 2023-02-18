package nl.rwslinkman.simdeviceble.service.devicetime

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

class DeviceTimeFeatureCharacteristic: Characteristic {
    override val name: String
        get() = "DeviceTimeFeatureCharacteristic"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("2B8E")
    override val type: Characteristic.Type
        get() = Characteristic.Type.Number
    override val isIndicate: Boolean
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