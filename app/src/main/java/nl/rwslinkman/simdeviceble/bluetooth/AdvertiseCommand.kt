package nl.rwslinkman.simdeviceble.bluetooth

import nl.rwslinkman.simdeviceble.device.model.Device

data class AdvertiseCommand(val device: Device, val includeDeviceName: Boolean, val isConnectable: Boolean) {
}