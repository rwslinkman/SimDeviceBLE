package nl.rwslinkman.simdeviceble.service.bloodpressure

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

class BloodPressureMeasurementCharacteristic: Characteristic {
    override val name: String
        get() = "BloodPressureMeasurementCharacteristic"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("2A35")
    override val type: Characteristic.Type
        get() = Characteristic.Type.Text

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