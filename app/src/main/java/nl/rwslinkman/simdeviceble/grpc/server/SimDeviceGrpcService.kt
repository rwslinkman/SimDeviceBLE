package nl.rwslinkman.simdeviceble.grpc.server

import android.util.Log
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver

class SimDeviceGrpcService : SimDeviceBLEGrpc.SimDeviceBLEImplBase() {
    override fun listAvailableSimDevices(
        request: Empty?,
        responseObserver: StreamObserver<ListAvailableSimDevicesResponse>?
    ) {
        super.listAvailableSimDevices(request, responseObserver)
        Log.i(TAG, "incoming request: listAvailableSimDevices")
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
}