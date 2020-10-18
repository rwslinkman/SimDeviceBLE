package nl.rwslinkman.simdeviceble.service.battery

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

class BatteryLevelCharacteristic: Characteristic {
    override val name: String
        get() = "BatteryLevel"

    override val uuid: UUID
        get() = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb")

    override val isRead: Boolean
        get() = true

    override val isNotify: Boolean
        get() = true

    override val description: String?
        get() = BATTERY_LEVEL_DESCRIPTION

    override val initialValue: ByteArray?
        get() = convertToBytes(INITIAL_BATTERY_LEVEL.toString())

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        value?.let {
            val batteryPercentage: Int = value[0].toInt()
            return if(batteryPercentage in 0..BATTERY_LEVEL_MAX) {
                BluetoothGatt.GATT_SUCCESS
            } else {
                BluetoothGatt.GATT_FAILURE
            }

        }
        return BluetoothGatt.GATT_FAILURE
    }

    override fun convertToPresentable(value: ByteArray): String {
        val batteryValue = value.first().toInt()
        return "$batteryValue%"
    }

    override fun convertToBytes(value: String): ByteArray {
        val batteryValue: Int = value.toInt()
        val retVal = ByteArray(1)
        retVal[0] = batteryValue.toByte()
        return retVal
    }

    companion object {
        private const val INITIAL_BATTERY_LEVEL = 50
        private const val BATTERY_LEVEL_MAX = 100
        private const val BATTERY_LEVEL_DESCRIPTION = "The current charge level of a " +
                "battery. 100% represents fully charged while 0% represents fully discharged."
    }
}