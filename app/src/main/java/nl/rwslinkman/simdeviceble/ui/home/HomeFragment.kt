package nl.rwslinkman.simdeviceble.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import nl.rwslinkman.simdeviceble.AppModel
import nl.rwslinkman.simdeviceble.R

class HomeFragment : Fragment() {

    private val model: AppModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // TODO: Add status row for location permission
        // TODO: Add button to enable bluetooth on phone
        // TODO: Add button to start permission flow

        // TODO: Add advertisement status card (gray out if disabled)
        // TODO: Add device selector
        // TODO: Add connectable switch
        // TODO: Add broadcast device name switch
        // TODO: Add button to start/stop advertising

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val bleSupportedTextView: TextView = root.findViewById(R.id.ble_status_supported_value)
        val bleEnabledTextView: TextView = root.findViewById(R.id.ble_status_enabled_value)
        val bleAdvertisingSupportedTextView: TextView = root.findViewById(R.id.ble_status_advertising_value)

        model.bluetoothSupported.observe(this, Observer {
            if (it) {
                bleSupportedTextView.text = "Yes"
            } else {
                bleSupportedTextView.text = "No"
            }
        })

        model.bluetoothEnabled.observe(this, Observer {
            if (it) {
                bleEnabledTextView.text = "Yes"
            } else {
                bleEnabledTextView.text = "No"
            }
        })

        model.bluetoothAdvertisingSupported.observe(this, Observer {
            if (it) {
                bleAdvertisingSupportedTextView.text = "Yes"
            } else {
                bleAdvertisingSupportedTextView.text = "No"
            }
        })

        return root
    }
}