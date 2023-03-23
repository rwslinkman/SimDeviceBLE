package nl.rwslinkman.simdeviceble.cucumbertest.test.grpc

import com.google.protobuf.ByteString
import com.google.protobuf.Empty
import io.grpc.ManagedChannelBuilder
import nl.rwslinkman.simdeviceble.grpc.server.*

class SimDeviceGrpcClient(targetIP: String, targetPort: Int) {

    private val commChannel = ManagedChannelBuilder.forAddress(targetIP, targetPort).usePlaintext().build()
    private val grpcStub = SimDeviceBLEGrpc.newBlockingStub(commChannel)

    fun listAvailableSimDevices(): List<SimDevice> {
        return grpcStub.listAvailableSimDevices(Empty.getDefaultInstance()).availableDevicesList.map {
            SimDevice(it.name, it.primaryServiceUUID)
        }
    }

    fun startAdvertisement(advertisedDevice: String, advertiseDeviceName: Boolean = true, connectable: Boolean = true): StartAdvertisementResponse? {
        val request = StartAdvertisementRequest.newBuilder()
        request.deviceName = advertisedDevice
        request.advertiseDeviceName = advertiseDeviceName
        request.connectable = connectable
        return grpcStub.startAdvertisement(request.build())
    }

    fun stopAdvertisement() {
        grpcStub.stopAdvertisement(Empty.getDefaultInstance())
    }

    fun listAdvertisedCharacteristics(): List<AdvertisedCharacteristic> {
        val listAdvertisedCharacteristics = grpcStub.listAdvertisedCharacteristics(Empty.getDefaultInstance())
        return listAdvertisedCharacteristics.advertisedCharacteristicsList.map {
            AdvertisedCharacteristic(it.name, it.uuid, it.currentValue.toByteArray())
        }
    }

    fun updateCharacteristicValue(uuid: String, updatedValue: ByteArray) {
        val request = UpdateCharacteristicValueRequest.newBuilder()
        request.uuid = uuid
        request.updatedValue = ByteString.copyFrom(updatedValue)
        grpcStub.updateCharacteristicValue(request.build())
    }

    fun notifyCharacteristic(uuid: String) {
        val request = NotifyCharacteristicRequest.newBuilder()
        request.uuid = uuid
        grpcStub.notifyCharacteristic(request.build())
    }

    fun shutdown() {
        commChannel.shutdownNow()
    }
}