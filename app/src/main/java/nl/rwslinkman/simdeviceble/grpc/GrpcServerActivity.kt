package nl.rwslinkman.simdeviceble.grpc

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.bluetooth.AdvertisementManager
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.grpc.server.GrpcCall
import nl.rwslinkman.simdeviceble.grpc.server.GrpcEventListener
import nl.rwslinkman.simdeviceble.grpc.server.GrpcServer
import java.util.*

class GrpcServerActivity : AppCompatActivity() {
    // grpc
    private val grpcServer = GrpcServer(8910)
    private val grpcDataContainer: MutableMap<UUID, ByteArray> = mutableMapOf()
    // ble
    private var isBluetoothSupported = false
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var advManager: AdvertisementManager? = null
    private var advertisementName: String? = null
    private var isAdvertisingSupported: Boolean = false
    // ui
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

    private val advManListener = object : AdvertisementManager.Listener {
        override fun updateDataContainer(characteristic: Characteristic, data: ByteArray) {
            TODO("Not yet implemented")
        }

        override fun setIsAdvertising(isAdvertising: Boolean) {
            TODO("Not yet implemented")
        }

        override fun onDeviceConnected(deviceAddress: String) {
            TODO("Not yet implemented")
        }

        override fun onDeviceDisconnected(deviceAddress: String) {
            TODO("Not yet implemented")
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

        setupBluetooth()
    }

    override fun onResume() {
        super.onResume()
//        grpcServer.actionHandler =
        grpcServer.eventListener = eventListener
        grpcServer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        grpcServer.stop()
    }

    private fun setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        isBluetoothSupported = bluetoothAdapter != null

        bluetoothAdapter?.let {
            advManager =
                AdvertisementManager(
                    this,
                    it,
                    advManListener
                )

            isAdvertisingSupported = it.isMultipleAdvertisementSupported
            advertisementName = it.name
        }
    }
}