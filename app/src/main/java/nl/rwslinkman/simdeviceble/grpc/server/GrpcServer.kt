package nl.rwslinkman.simdeviceble.grpc.server

import io.grpc.Server
import io.grpc.netty.NettyServerBuilder
import java.util.*

class GrpcServer(private val port: Int = 8765) {
    var eventListener: GrpcEventListener? = null
    var actionHandler: GrpcActionHandler? = null
    private lateinit var server: Server

    private val nullActionHandler = object : GrpcActionHandler {
        override fun getSupportedDevices(): List<SimDevice> {
            return emptyList()
        }

        override fun startAdvertisement(command: AdvertisementStartCommand): AdvertisementData {
            return AdvertisementData(
                advertisementName = "n/a",
                primaryServiceUUID = UUID.randomUUID(),
                isConnectable = false,
                isAdvertisingDeviceName = false
            )
        }

        override fun stopAdvertisement() {
            // NOP
        }

        override fun listAdvertisedCharacteristics(): List<Characteristic> {
            return emptyList()
        }

        override fun updateCharacteristicValue(uuid: String?, data: ByteArray?) {
            // NOP
        }

        override fun notifyCharacteristic(uuid: String?) {
            // NOP
        }
    }

    fun start() {
        server = NettyServerBuilder
            .forPort(port)
            .addService(createService())
            .build()
        server.start()
        eventListener?.onGrpcServerStarted()
    }

    fun stop() {
        server.shutdown()
        eventListener?.onGrpcServerStopped()
    }

    private fun createService(): SimDeviceGrpcService {
        val handler: GrpcActionHandler =
            if (this.actionHandler == null) nullActionHandler else this.actionHandler!!
        return SimDeviceGrpcService(actionHandler = handler, eventListener = eventListener)
    }
}