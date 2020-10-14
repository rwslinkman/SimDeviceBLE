package nl.rwslinkman.simdeviceble.device.model

import nl.rwslinkman.simdeviceble.device.heartrate.HeartRatePeripheral
import nl.rwslinkman.simdeviceble.device.model.Device

object Devices {
    val supportedDevices: List<Device> = listOf(
        HeartRatePeripheral()
    )
}