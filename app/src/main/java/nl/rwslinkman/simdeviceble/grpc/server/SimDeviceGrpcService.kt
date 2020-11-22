package nl.rwslinkman.simdeviceble.grpc.server

import android.util.Log
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Device

/**
 * class SimDeviceGrpcService
 * Handles incoming calls from gRPC clients.
 * When unable to compile, see "<projectroot>/grpc/generate_java.sh" for code generation
 */
class SimDeviceGrpcService(
    val actionHandler: GrpcActionHandler,
    private val eventListener: GrpcEventListener?
) : SimDeviceBLEGrpc.SimDeviceBLEImplBase() {
    override fun listAvailableSimDevices(
        request: Empty?,
        responseObserver: StreamObserver<ListAvailableSimDevicesResponse>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.ListAvailableSimDevices)
        Log.i(TAG, "incoming request: listAvailableSimDevices")

        val allSimDevices = actionHandler.getSupportedDevices()

        val responseBuilder = ListAvailableSimDevicesResponse
            .newBuilder()
            .addAllAvailableDevices(allSimDevices)
        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }

    override fun startAdvertisement(
        request: Empty?,
        responseObserver: StreamObserver<StartAdvertisementResponse>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.StartAdvertisement)
        Log.i(TAG, "incoming request: startAdvertisement")
        super.startAdvertisement(request, responseObserver)

    }

    override fun stopAdvertisement(
        request: Empty?,
        responseObserver: StreamObserver<Empty>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.StopAdvertisement)
        Log.i(TAG, "incoming request: stopAdvertisement")
        super.stopAdvertisement(request, responseObserver)
    }

    override fun listAdvertisedCharacteristics(
        request: Empty?,
        responseObserver: StreamObserver<ListAdvertisedCharacteristicsResponse>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.ListAdvertisedCharacteristics)
        Log.i(TAG, "incoming request: listAdvertisedCharacteristics")
        super.listAdvertisedCharacteristics(request, responseObserver)
    }

    override fun updateCharacteristicValue(
        request: UpdateCharacteristicValueRequest?,
        responseObserver: StreamObserver<UpdateCharacteristicValueResponse>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.UpdateCharacteristicValue)
        Log.i(TAG, "incoming request: updateCharacteristicValue")
        super.updateCharacteristicValue(request, responseObserver)
    }

    override fun notifyCharacteristic(
        request: NotifyCharacteristicRequest?,
        responseObserver: StreamObserver<Empty>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.NotifyCharacteristic)
        Log.i(TAG, "incoming request: notifyCharacteristic")
        super.notifyCharacteristic(request, responseObserver)
    }

    companion object {
        const val TAG = "SimDeviceGrpcService"
    }

//    private fun convert(device: Device): SimDevice {
//        val simDeviceBuilder = SimDevice.newBuilder()
//            .setName(device.name)
//            .setPrimaryServiceUUID(device.primaryServiceUuid.toString())
//
//        device.services.map(::convertService).apply {
//            simDeviceBuilder.addAllServices(this)
//        }
//        return simDeviceBuilder.build()
//    }

//    private fun convertService(service: nl.rwslinkman.simdeviceble.device.model.Service) : Service {
//        val simServiceBuilder = Service.newBuilder()
//            .setName(service.name)
//            .setUuid(service.uuid.toString())
//
//        service.characteristics.map(::convertChar).apply {
//            simServiceBuilder.addAllCharacteristics(this)
//        }
//
//        return simServiceBuilder.build()
//    }

//    private fun convertChar(characteristic: Characteristic): nl.rwslinkman.simdeviceble.grpc.server.Characteristic {
//        val simCharBuilder = nl.rwslinkman.simdeviceble.grpc.server.Characteristic.newBuilder()
//            .setName(characteristic.name)
//            .setUuid(characteristic.uuid.toString())
//        return simCharBuilder.build()
//    }
}