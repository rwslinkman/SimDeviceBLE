package nl.rwslinkman.simdeviceble.grpc

import nl.rwslinkman.simdeviceble.AppModel
import nl.rwslinkman.simdeviceble.bluetooth.AdvertisementManager
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.grpc.server.*
import java.lang.IllegalArgumentException
import java.util.*

// TODO: Implement data handling
class GrpcDataModel: AdvertisementManager.Listener, GrpcActionHandler {
    private val dataContainer: MutableMap<UUID, ByteArray> = mutableMapOf()

    override fun updateDataContainer(characteristic: Characteristic, data: ByteArray) {
        // TODO
    }

    override fun setIsAdvertising(isAdvertising: Boolean) {
        // TODO
    }

    override fun onDeviceConnected(deviceAddress: String) {
        // TODO
    }

    override fun onDeviceDisconnected(deviceAddress: String) {
        // TODO
    }

    override fun getSupportedDevices(): List<SimDevice> {
        val devices: List<Device> = AppModel.supportedDevices
        return devices.map(::convertDevice)
    }

    override fun startAdvertisement(command: AdvertisementStartCommand): AdvertisementData {
        // TODO
//        throw IllegalArgumentException() // test
        return AdvertisementData("GRPCDevice", UUID.randomUUID(), false, false)
    }

    override fun stopAdvertisement() {
        // TODO
    }

    override fun listAdvertisedCharacteristics(): List<nl.rwslinkman.simdeviceble.grpc.server.Characteristic> {
        return emptyList()
    }

    override fun getCharacteristicValue(uuid: String): ByteArray {
        // TODO
        return byteArrayOf()
    }

    override fun updateCharacteristicValue(uuid: String?, data: ByteArray?) {
        // TODO
    }

    override fun notifyCharacteristic(uuid: String?) {
        // TODO
    }

    private fun convertDevice(device: Device): SimDevice {
        val simDeviceBuilder = SimDevice.newBuilder()
            .setName(device.name)
            .setPrimaryServiceUUID(device.primaryServiceUuid.toString())

        device.services.map(::convertService).apply {
            simDeviceBuilder.addAllServices(this)
        }
        return simDeviceBuilder.build()
    }

    private fun convertService(service: nl.rwslinkman.simdeviceble.device.model.Service) : Service {
        val simServiceBuilder = Service.newBuilder()
            .setName(service.name)
            .setUuid(service.uuid.toString())

        service.characteristics.map(::convertChar).apply {
            simServiceBuilder.addAllCharacteristics(this)
        }

        return simServiceBuilder.build()
    }

    private fun convertChar(characteristic: Characteristic): nl.rwslinkman.simdeviceble.grpc.server.Characteristic {
        val simCharBuilder = nl.rwslinkman.simdeviceble.grpc.server.Characteristic.newBuilder()
            .setName(characteristic.name)
            .setUuid(characteristic.uuid.toString())
        return simCharBuilder.build()
    }
}