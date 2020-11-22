package nl.rwslinkman.simdeviceble.grpc.server

enum class GrpcCall {
    ListAvailableSimDevices,
    StartAdvertisement,
    StopAdvertisement,
    ListAdvertisedCharacteristics,
    UpdateCharacteristicValue
}

interface GrpcEventListener {
    fun onGrpcServerStarted()
    fun onGrpcServerStopped()
    fun onGrpcCallReceived(event: GrpcCall)
}