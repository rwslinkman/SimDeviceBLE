package nl.rwslinkman.simdeviceble.bluetooth

import nl.rwslinkman.simdeviceble.device.model.Device
import java.util.*

interface BluetoothDelegate {

    fun turnOnBluetooth()

    fun advertise(device: Device, includeDeviceName: Boolean, isConnectable: Boolean)

    fun stopAdvertising()

    fun updateCharacteristicValues(characteristicData: MutableMap<UUID, ByteArray>)
}