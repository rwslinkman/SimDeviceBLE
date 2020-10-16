package nl.rwslinkman.simdeviceble.device.model

import android.bluetooth.BluetoothGattCharacteristic
import java.util.*

interface Characteristic {
    val uuid: UUID
    val isRead: Boolean
        get() = false
    val isWrite: Boolean
        get() = false
    val isNotify: Boolean
        get() = false
    val isIndicate: Boolean
        get() = false

    fun validateWrite(): Int
}