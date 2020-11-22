package nl.rwslinkman.simdeviceble.grpc

import nl.rwslinkman.simdeviceble.bluetooth.AdvertisementManager
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.grpc.server.AdvertisementData
import nl.rwslinkman.simdeviceble.grpc.server.AdvertisementStartCommand
import nl.rwslinkman.simdeviceble.grpc.server.GrpcActionHandler
import nl.rwslinkman.simdeviceble.grpc.server.SimDevice
import java.util.*

class GrpcDataModel: AdvertisementManager.Listener, GrpcActionHandler {
    override fun updateDataContainer(characteristic: Characteristic, data: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun setIsAdvertising(isAdvertising: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onDeviceConnected(deviceAddress: String) {
        TODO("Not yet implemented")
    }

    override fun onDeviceDisconnected(deviceAddress: String) {
        TODO("Not yet implemented")
    }

    override fun getSupportedDevices(): List<SimDevice> {
        TODO("Not yet implemented")
    }

    override fun startAdvertisement(command: AdvertisementStartCommand): AdvertisementData {
        TODO("Not yet implemented")
    }

    override fun stopAdvertisement() {
        TODO("Not yet implemented")
    }

    override fun getCharacteristicValue(uuid: UUID): ByteArray {
        TODO("Not yet implemented")
    }

    override fun updateCharacteristicValue(uuid: UUID, data: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun notifyCharacteristic(uuid: UUID) {
        TODO("Not yet implemented")
    }
}