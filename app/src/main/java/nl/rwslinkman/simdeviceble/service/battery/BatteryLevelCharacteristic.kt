package nl.rwslinkman.simdeviceble.service.battery

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.util.*

class BatteryLevelCharacteristic: Characteristic {

    override val uuid: UUID
        get() = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb")

    override val isRead: Boolean
        get() = true

    override val isNotify: Boolean
        get() = true

    override fun validateWrite(): Int {
        // TODO
        return BluetoothGatt.GATT_SUCCESS
    }

    companion object {
        private const val INITIAL_BATTERY_LEVEL = 50
        private const val BATTERY_LEVEL_MAX = 100
    }
}