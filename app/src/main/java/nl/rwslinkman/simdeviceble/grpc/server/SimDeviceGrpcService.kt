package nl.rwslinkman.simdeviceble.grpc.server

import android.util.Log
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver

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
        request: StartAdvertisementRequest?,
        responseObserver: StreamObserver<StartAdvertisementResponse>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.StartAdvertisement)
        Log.i(TAG, "incoming request: startAdvertisement")

        try {
            val deviceName = request?.deviceName
            val connectable = request?.connectable
            val advertiseDeviceName = request?.advertiseDeviceName

            val command = AdvertisementStartCommand(deviceName, connectable, advertiseDeviceName)
            val advertisementData = actionHandler.startAdvertisement(command)

            val responseBuilder = StartAdvertisementResponse.newBuilder()
                .setAdvertisementName(advertisementData.advertisementName)
                .setIsConnectable(advertisementData.isConnectable)
                .setIsAdvertisingDeviceName(advertisementData.isAdvertisingDeviceName)
                .setPrimaryServiceUUID(advertisementData.primaryServiceUUID.toString())
            responseObserver?.onNext(responseBuilder.build())
            responseObserver?.onCompleted()
        } catch (t: Throwable) {
            responseObserver?.onError(t)
        }
    }

    override fun stopAdvertisement(
        request: Empty?,
        responseObserver: StreamObserver<Empty>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.StopAdvertisement)
        Log.i(TAG, "incoming request: stopAdvertisement")

        try {
            actionHandler.stopAdvertisement()

            val responseBuilder = Empty.newBuilder()
            responseObserver?.onNext(responseBuilder.build())
            responseObserver?.onCompleted()
        } catch (t: Throwable) {
            responseObserver?.onError(t)
        }
    }

    override fun listAdvertisedCharacteristics(
        request: Empty?,
        responseObserver: StreamObserver<ListAdvertisedCharacteristicsResponse>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.ListAdvertisedCharacteristics)
        Log.i(TAG, "incoming request: listAdvertisedCharacteristics")

        try {
            val characteristics: List<Characteristic> =
                actionHandler.listAdvertisedCharacteristics()

            val responseBuilder = ListAdvertisedCharacteristicsResponse.newBuilder()
            characteristics.forEach {
                responseBuilder.addAdvertisedCharacteristics(it)
            }

            responseObserver?.onNext(responseBuilder.build())
            responseObserver?.onCompleted()
        } catch (t: Throwable) {
            responseObserver?.onError(t)
        }
    }

    override fun updateCharacteristicValue(
        request: UpdateCharacteristicValueRequest?,
        responseObserver: StreamObserver<Empty>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.UpdateCharacteristicValue)
        Log.i(TAG, "incoming request: updateCharacteristicValue")

        try {
            val uuid = request?.uuid
            val updateValue = request?.updatedValue?.toByteArray()
            actionHandler.updateCharacteristicValue(uuid, updateValue)

            responseObserver?.onNext(Empty.newBuilder().build())
            responseObserver?.onCompleted()
        } catch (t: Throwable) {
            responseObserver?.onError(t)
        }
    }

    override fun notifyCharacteristic(
        request: NotifyCharacteristicRequest?,
        responseObserver: StreamObserver<Empty>?
    ) {
        eventListener?.onGrpcCallReceived(GrpcCall.NotifyCharacteristic)
        Log.i(TAG, "incoming request: notifyCharacteristic")

        try {
            val uuid = request?.uuid

            actionHandler.notifyCharacteristic(uuid)

            val responseBuilder = Empty.newBuilder()
            responseObserver?.onNext(responseBuilder.build())
            responseObserver?.onCompleted()
        } catch (t: Throwable) {
            responseObserver?.onError(t)
        }
    }

    companion object {
        const val TAG = "SimDeviceGrpcService"
    }
}