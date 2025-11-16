package nl.rwslinkman.simdeviceble

import nl.rwslinkman.simdeviceble.device.DigitalClock
import nl.rwslinkman.simdeviceble.device.EarThermometer
import nl.rwslinkman.simdeviceble.device.HeartRatePeripheral
import nl.rwslinkman.simdeviceble.device.RoomSensorDevice
import nl.rwslinkman.simdeviceble.device.model.Device

object SimDevices {
    val supportedDevices: List<Device> = listOf(
        HeartRatePeripheral(),
        DigitalClock(),
        EarThermometer(),
        RoomSensorDevice()
    )
}