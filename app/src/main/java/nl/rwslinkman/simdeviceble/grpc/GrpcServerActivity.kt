package nl.rwslinkman.simdeviceble.grpc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.grpc.server.GrpcServer

class GrpcServerActivity : AppCompatActivity() {
    private val grpcServer = GrpcServer(8910)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grpc_server)

        grpcServer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        grpcServer.stopServer()
    }
}