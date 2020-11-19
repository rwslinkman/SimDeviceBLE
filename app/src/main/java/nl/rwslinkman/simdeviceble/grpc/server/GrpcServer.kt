package nl.rwslinkman.simdeviceble.grpc.server

import io.grpc.Server
import io.grpc.ServerBuilder

class GrpcServer(private val port: Int = 0) {
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(SimDeviceGrpcService())
        .build()

    fun start() {
        server.start()
        server.awaitTermination()
    }

    fun stop() {

    }
}