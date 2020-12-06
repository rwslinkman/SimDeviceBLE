package nl.rwslinkman.simdeviceble.bluetooth

import java.util.*

class BluetoothUUID {
    companion object {
        fun fromSigNumber(sigNumber: String): UUID {
            return UUID.fromString("0000$sigNumber-0000-1000-8000-00805f9b34fb")
        }
    }
}
