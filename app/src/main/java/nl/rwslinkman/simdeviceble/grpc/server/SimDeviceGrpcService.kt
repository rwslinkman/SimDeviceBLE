package nl.rwslinkman.simdeviceble.grpc.server

import android.util.Log
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import nl.rwslinkman.simdeviceble.AppModel
import nl.rwslinkman.simdeviceble.device.model.Device

/**
 * class SimDeviceGrpcService
 * Handles incoming calls from gRPC clients.
 * When unable to compile, see "<projectroot>/grpc/generate_java.sh" for code generation
 */
class SimDeviceGrpcService(
    val eventListener: GrpcEventListener?,
    val actionHandler: GrpcActionHandler?
) : SimDeviceBLEGrpc.SimDeviceBLEImplBase() {
    override fun listAvailableSimDevices(
        request: Empty?,
        responseObserver: StreamObserver<ListAvailableSimDevicesResponse>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.ListAvailableSimDevices)
        Log.i(TAG, "incoming request: listAvailableSimDevices")

        val allSimDevices = AppModel.supportedDevices.map(::convert)

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
        responseObserver: StreamObserver<StopAdvertisementResponse>?
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

    companion object {
        const val TAG = "SimDeviceGrpcService"
    }

    private fun convert(device: Device): SimDevice {
        return SimDevice.newBuilder()
            .setName(device.name)
            .setPrimaryServiceUUID(device.primaryServiceUuid.toString())
            .build()
    }
}