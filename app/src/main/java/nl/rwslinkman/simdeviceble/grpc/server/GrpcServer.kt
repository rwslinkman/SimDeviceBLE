package nl.rwslinkman.simdeviceble.grpc.server

import io.grpc.Server
import io.grpc.netty.NettyServerBuilder

class GrpcServer(private val port: Int = 8765) {
    var eventListener: GrpcEventListener? = null
    var actionHandler: GrpcActionHandler? = null
    private lateinit var server: Server

    fun start() {
        server = NettyServerBuilder
            .forPort(port)
            .addService(SimDeviceGrpcService(eventListener, actionHandler))
            .build()
        server.start()
        eventListener?.onGrpcServerStarted()
    }

    fun stop() {
        server.shutdown()
        eventListener?.onGrpcServerStopped()
    }
}