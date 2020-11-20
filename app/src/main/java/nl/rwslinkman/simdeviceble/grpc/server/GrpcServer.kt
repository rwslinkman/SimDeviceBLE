package nl.rwslinkman.simdeviceble.grpc.server

import io.grpc.Server
import io.grpc.netty.NettyServerBuilder

class GrpcServer(private val port: Int = 8765) {

    private lateinit var server: Server

    fun start() {
        server = NettyServerBuilder
            .forPort(port)
            .addService(SimDeviceGrpcService())
            .build()
    }

    fun stopServer() {
        server.shutdown()
    }
}