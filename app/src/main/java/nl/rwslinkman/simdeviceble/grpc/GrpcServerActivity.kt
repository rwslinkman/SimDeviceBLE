package nl.rwslinkman.simdeviceble.grpc

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.grpc.server.GrpcCall
import nl.rwslinkman.simdeviceble.grpc.server.GrpcEventListener
import nl.rwslinkman.simdeviceble.grpc.server.GrpcServer

class GrpcServerActivity : AppCompatActivity() {
    private val grpcServer = GrpcServer(8910)
    private lateinit var statusSubtitle: TextView
    private val grpcEventAdapter = EventListAdapter()

    private val eventListener = object : GrpcEventListener {
        override fun onGrpcServerStarted() {
            statusSubtitle.text = getText(R.string.grpc_server_running)
            addEventToView("gRPC server has started")
        }

        override fun onGrpcServerStopped() {
            statusSubtitle.text = getText(R.string.grpc_server_idle)
            addEventToView("gRPC server was stopped")
        }

        override fun onGrpcCallReceived(event: GrpcCall) {
            val eventDetails = "Received call: ${event.name}"
            addEventToView(eventDetails)
        }

        private fun addEventToView(eventDetails: String) {
            runOnUiThread {
                grpcEventAdapter.addGrpcEvent(eventDetails)
                grpcEventAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grpc_server)

        statusSubtitle = findViewById(R.id.simdeviceble_grpc_subtitle)

        findViewById<RecyclerView>(R.id.simdeviceble_grpc_eventlist).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = grpcEventAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        grpcServer.eventListener = eventListener
        grpcServer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        grpcServer.stop()
    }
}