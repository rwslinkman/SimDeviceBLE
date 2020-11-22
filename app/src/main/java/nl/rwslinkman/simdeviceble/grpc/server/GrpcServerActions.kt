package nl.rwslinkman.simdeviceble.grpc.server

import nl.rwslinkman.simdeviceble.device.model.Device
import java.util.*

data class AdvertisementData(val isAdvertising: Boolean, val advertisementName: String, val isConnectable: Boolean, val isAdvertisingDeviceName: Boolean)
data class AdvertisementStartCommand(val device: Device, val connectable: Boolean, val advertiseDeviceName: Boolean)

interface GrpcActionHandler {

    fun getSupportedDevices(): List<SimDevice>
    fun startAdvertisement(command: AdvertisementStartCommand): AdvertisementData
    fun stopAdvertisement()
    fun getCharacteristicValue(uuid: UUID): ByteArray
    fun updateCharacteristicValue(uuid: UUID, data: ByteArray)
    fun notifyCharacteristic(uuid: UUID)
}