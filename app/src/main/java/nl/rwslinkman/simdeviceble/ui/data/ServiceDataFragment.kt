package nl.rwslinkman.simdeviceble.ui.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.AppModel
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.device.model.Device

class ServiceDataFragment : Fragment() {

    private val appModel: AppModel by activityViewModels()
    private val servicesAdapter = ServiceDataAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: Show characteristics and allow manipulation
        val root = inflater.inflate(R.layout.fragment_servicedata, container, false)

        val notAdvertisingView: TextView = root.findViewById(R.id.block_not_advertising)
        val servicesView: RelativeLayout = root.findViewById(R.id.block_services)

        // List with subscription to auto update itself
        root.findViewById<RecyclerView>(R.id.servicedata_list).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = servicesAdapter
        }
        appModel.activeDevice.observe(this, Observer {
            showDeviceServices(it)
        })

        // Toggle view visibility
        appModel.isAdvertising.observe(this, Observer {
            notAdvertisingView.visibility = if (it) View.GONE else View.VISIBLE
            servicesView.visibility = if (it) View.VISIBLE else View.GONE
        })
        return root
    }

    private fun showDeviceServices(device: Device) {
        val isAdvertising: Boolean = appModel.isAdvertising.value ?: false
        if (!isAdvertising) {
            return
        }

        servicesAdapter.updateDataSet(device.services)
    }
}