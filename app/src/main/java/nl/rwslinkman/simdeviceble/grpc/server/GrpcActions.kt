package nl.rwslinkman.simdeviceble.grpc.server

import java.util.*

data class AdvertisementData(val advertisementName: String, val primaryServiceUUID: UUID, val isConnectable: Boolean, val isAdvertisingDeviceName: Boolean)
data class AdvertisementStartCommand(val device: String?, val connectable: Boolean?, val advertiseDeviceName: Boolean?)

interface GrpcActionHandler {

    fun getSupportedDevices(): List<SimDevice>
    fun startAdvertisement(command: AdvertisementStartCommand): AdvertisementData
    fun stopAdvertisement()
    fun listAdvertisedCharacteristics(): List<Characteristic>
    fun updateCharacteristicValue(uuid: String?, data: ByteArray?)
    fun notifyCharacteristic(uuid: String?)
}