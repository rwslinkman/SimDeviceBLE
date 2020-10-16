package nl.rwslinkman.simdeviceble.bluetooth

import nl.rwslinkman.simdeviceble.device.model.Device

interface BluetoothDelegate {

    fun turnOnBluetooth()

    fun advertise(device: Device, includeDeviceName: Boolean, isConnectable: Boolean)

    fun stopAdvertising()
}