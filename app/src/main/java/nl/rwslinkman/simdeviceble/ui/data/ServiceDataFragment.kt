package nl.rwslinkman.simdeviceble.ui.data

import android.os.Bundle
import android.text.Editable
import android.util.Log
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
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Device
import java.util.*

class ServiceDataFragment : Fragment() {

    private val manipulationListener = object : ServiceDataAdapter.CharacteristicManipulationListener {
        override fun setCharacteristicValue(characteristic: Characteristic, setValue: Any) {
            Log.d(TAG, "setCharacteristicValue: set value to $setValue")
            appModel.updateCharacteristicValue(characteristic, setValue.toString())
        }

        override fun notifyCharacteristic(characteristic: Characteristic) {
            Log.d(TAG, "notifyCharacteristic: send notification")
            appModel.sendCharacteristicNotification(characteristic)
        }

    }
    private val appModel: AppModel by activityViewModels()
    private val servicesAdapter = ServiceDataAdapter(manipulationListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            showDeviceServices()
        })
        appModel.presentableDataContainer.observe(this, Observer {
            showDeviceServices()
        })

        // Toggle view visibility
        appModel.isAdvertising.observe(this, Observer {
            notAdvertisingView.visibility = if (it) View.GONE else View.VISIBLE
            servicesView.visibility = if (it) View.VISIBLE else View.GONE
        })
        return root
    }

    private fun showDeviceServices() {
        val isAdvertising: Boolean = appModel.isAdvertising.value ?: false
        if (!isAdvertising) {
            return
        }

        val activeDevice: Device = appModel.activeDevice.value!!
        val currentData: Map<UUID, String> = appModel.presentableDataContainer.value!!

        servicesAdapter.updateDataSet(activeDevice.services, currentData)
    }

    companion object {
        const val TAG = "ServiceDataFragment"
    }
}