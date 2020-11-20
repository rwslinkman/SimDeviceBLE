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
class SimDeviceGrpcService : SimDeviceBLEGrpc.SimDeviceBLEImplBase() {
    override fun listAvailableSimDevices(
        request: Empty?,
        responseObserver: StreamObserver<ListAvailableSimDevicesResponse>?
    ) {
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
        super.startAdvertisement(request, responseObserver)
        Log.i(TAG, "incoming request: startAdvertisement")
    }

    override fun stopAdvertisement(
        request: Empty?,
        responseObserver: StreamObserver<StopAdvertisementResponse>?
    ) {
        super.stopAdvertisement(request, responseObserver)
        Log.i(TAG, "incoming request: stopAdvertisement")
    }

    override fun listAdvertisedCharacteristics(
        request: Empty?,
        responseObserver: StreamObserver<ListAdvertisedCharacteristicsResponse>?
    ) {
        super.listAdvertisedCharacteristics(request, responseObserver)
        Log.i(TAG, "incoming request: listAdvertisedCharacteristics")
    }

    override fun updateCharacteristicValue(
        request: UpdateCharacteristicValueRequest?,
        responseObserver: StreamObserver<UpdateCharacteristicValueResponse>?
    ) {
        super.updateCharacteristicValue(request, responseObserver)
        Log.i(TAG, "incoming request: updateCharacteristicValue")
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