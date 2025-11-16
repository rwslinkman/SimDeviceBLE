package nl.rwslinkman.simdeviceble.grpc

import android.annotation.SuppressLint
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

@SuppressLint("MissingPermission")
class GrpcServerActivity : AppCompatActivity() {
    // grpc
    private val grpcPort = 8911
    private val grpcServer = GrpcServer(grpcPort)
    private lateinit var dataModel: GrpcDataModel

    // ble
    private var isBluetoothSupported = false
    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var advManager: AdvertisementManager
    private lateinit var hostData: HostData

    // ui
    private lateinit var statusSubtitle: TextView
    private lateinit var grpcEventListView: RecyclerView
    private val grpcEventAdapter = EventListAdapter()

    private val eventListener = object : GrpcEventListener {
        override fun onGrpcServerStarted() {
            statusSubtitle.text = getString(R.string.grpc_server_running, grpcPort)
            addEventToView("gRPC server has started")
        }

        override fun onGrpcServerStopped() {
            statusSubtitle.text = getText(R.string.grpc_server_idle)
            addEventToView("gRPC server was stopped")
        }

        override fun onGrpcCallReceived(event: GrpcCall) {
            val eventDetails = "Received gRPC event ${event.name}"
            addEventToView(eventDetails)
        }
    }

    private val btDelegate = object : AdvertisementManager.Listener {
        override fun updateDataContainer(characteristic: Characteristic, data: ByteArray, isInitialValue: Boolean) {
            if(isInitialValue) return
            addEventToView("Value of ${characteristic.name} was updated")
        }

        override fun setIsAdvertising(isAdvertising: Boolean, advertisedDevice: String?) {
            val event = if(isAdvertising) {
                "SimDeviceBLE has started advertising as a '${advertisedDevice ?: "unknown"}' device"
            }
            else "SimDeviceBLE has stopped advertising"
            addEventToView(event)
        }

        override fun onDeviceConnected(deviceAddress: String) {
            addEventToView("Device '$deviceAddress' has connected to SimDeviceBLE")
        }

        override fun onDeviceDisconnected(deviceAddress: String) {
            addEventToView("Device '$deviceAddress' has disconnected from SimDeviceBLE")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grpc_server)

        statusSubtitle = findViewById(R.id.simdeviceble_grpc_subtitle)

        grpcEventListView = findViewById<RecyclerView>(R.id.simdeviceble_grpc_eventlist)
        grpcEventListView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = grpcEventAdapter
        }

        setupBluetooth()
    }

    override fun onResume() {
        super.onResume()
        grpcServer.actionHandler = dataModel
        grpcServer.eventListener = eventListener
        grpcServer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        grpcServer.stop()
        advManager.stop()
    }

    private fun setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        isBluetoothSupported = bluetoothAdapter != null

        bluetoothAdapter?.let {
            advManager = AdvertisementManager(this, it, btDelegate)

            hostData = HostData(it.name, it.isMultipleAdvertisementSupported)
            dataModel = GrpcDataModel(advManager, hostData)
        }
    }

    private fun addEventToView(eventDetails: String) {
        runOnUiThread {
            grpcEventAdapter.addGrpcEvent(EventListAdapter.Item(eventDetails))
            val insertedPos = grpcEventAdapter.itemCount - 1
            grpcEventAdapter.notifyItemInserted(insertedPos)
            grpcEventListView.scrollToPosition(insertedPos)
        }
    }
}