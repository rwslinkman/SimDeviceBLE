package nl.rwslinkman.simdeviceble.grpc

import com.google.protobuf.ByteString
import nl.rwslinkman.simdeviceble.AppModel
import nl.rwslinkman.simdeviceble.SimDevices
import nl.rwslinkman.simdeviceble.bluetooth.AdvertiseCommand
import nl.rwslinkman.simdeviceble.bluetooth.AdvertisementManager
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.grpc.server.*
import java.util.*

class GrpcDataModel(private val advertisementManager: AdvertisementManager, private val hostData: HostData) :
    GrpcActionHandler {

    override fun getSupportedDevices(): List<SimDevice> {
        return allDevices().map(::convertDevice)
    }

    override fun startAdvertisement(command: AdvertisementStartCommand): AdvertisementData {
        val device: Device = allDevices().find { it.name == command.device }
            ?: throw IllegalArgumentException("Device '${command.device}' is not supported")

        val includesDeviceName = command.advertiseDeviceName ?: true
        val isConnectable = command.connectable ?: true
        val startCommand = AdvertiseCommand(
            device,
            includesDeviceName,
            isConnectable
        )
        advertisementManager.advertise(startCommand)
        return AdvertisementData(
            hostData.advertisementName,
            device.primaryServiceUuid,
            isConnectable,
            includesDeviceName
        )
    }

    override fun stopAdvertisement() {
        advertisementManager.stop()
    }

    override fun listAdvertisedCharacteristics(): List<nl.rwslinkman.simdeviceble.grpc.server.Characteristic> {
        val data = advertisementManager.getAdvertisedCharacteristicValues()
        return data.map {
            val byteString = ByteString.copyFrom(it.value)
            val builder = nl.rwslinkman.simdeviceble.grpc.server.Characteristic
                .newBuilder()
                .setUuid(it.key.uuid.toString())
                .setName(it.key.name)
                .setCurrentValue(byteString)
            builder.build()
        }
    }

    override fun updateCharacteristicValue(uuid: String?, data: ByteArray?) {
        val charUUID: UUID = UUID.fromString(uuid ?: throw IllegalArgumentException("Property 'uuid' is required"))
        val updateData: ByteArray = data ?: throw IllegalArgumentException("Property 'data' is required")

        val charMap = mutableMapOf(Pair(charUUID, updateData))
        advertisementManager.updateAdvertisedCharacteristics(charMap)
    }

    override fun notifyCharacteristic(uuid: String?) {
        val charUUID = UUID.fromString(uuid ?: throw IllegalArgumentException("Property 'uuid' is required"))
        advertisementManager.sendNotificationToConnectedDevices(charUUID)
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

    private fun convertService(service: nl.rwslinkman.simdeviceble.device.model.Service): Service {
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

    private fun allDevices(): List<Device> = SimDevices.supportedDevices
}