package nl.rwslinkman.simdeviceble.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import nl.rwslinkman.simdeviceble.AppModel
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.device.model.Device


class HomeFragment : Fragment() {

    private val appModel: AppModel by activityViewModels()
    private val selectorListener = object : OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.d(TAG, "onNothingSelected: ")
        }

        override fun onItemSelected(
            adapterView: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            val selectedItem: String = adapterView?.getItemAtPosition(position) as String
            updateSelectedDevice(selectedItem)
        }
    }
    private lateinit var deviceSelectorAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //region Bluetooth
        val bleSupportedTextView: TextView = root.findViewById(R.id.ble_status_supported_value)
        val bleEnabledTextView: TextView = root.findViewById(R.id.ble_status_enabled_value)
        val bleAdvertisingSupportedTextView: TextView =
            root.findViewById(R.id.ble_status_advertising_value)
        val enableBluetoothBtn = root.findViewById<Button>(R.id.ble_enable_btn)

        appModel.bluetoothSupported.observe(viewLifecycleOwner, Observer {
            bleSupportedTextView.text = if (it) "Yes" else "No"
        })

        appModel.bluetoothEnabled.observe(viewLifecycleOwner, Observer {
            bleEnabledTextView.text = if (it) "Yes" else "No"
        })

        appModel.bluetoothAdvertisingSupported.observe(viewLifecycleOwner, Observer {
            bleAdvertisingSupportedTextView.text = if (it) "Yes" else "No"
            enableBluetoothBtn.isEnabled = !it
        })

        enableBluetoothBtn.setOnClickListener {
            appModel.enableBluetooth()
        }
        //endregion

        // region Advertising
        val advertisingEnabledView: TextView = root.findViewById(R.id.advertising_enabled_value)
        val advertisementNameView: TextView = root.findViewById(R.id.advertising_name_value)
        val deviceSelector: Spinner = root.findViewById(R.id.advertising_device_value)
        val connectableSwitch: SwitchCompat = root.findViewById(R.id.advertising_connectable_value)
        val advertiseDeviceNameSwitch: SwitchCompat =
            root.findViewById(R.id.advertising_allowname_value)
        val advertiseStartBtn = root.findViewById<Button>(R.id.advertising_start_btn)
        val advertiseStopBtn = root.findViewById<Button>(R.id.advertising_stop_btn)

        appModel.isAdvertising.observe(viewLifecycleOwner, Observer {
            advertisingEnabledView.text = if (it) "Yes" else "No"
            // disable control while advertising
            deviceSelector.isEnabled = !it
            connectableSwitch.isEnabled = !it
            advertiseDeviceNameSwitch.isEnabled = !it
            advertiseStartBtn.isEnabled = !it
            advertiseStopBtn.isEnabled = it
        })

        appModel.advertisementName.observe(viewLifecycleOwner, Observer {
            advertisementNameView.text = it
        })

        setupDeviceSelector(deviceSelector)
        appModel.activeDevice.observe(viewLifecycleOwner, Observer {
                val selectedName = it.name
                val selectedItemPos = deviceSelectorAdapter.getPosition(selectedName)
                deviceSelector.setSelection(selectedItemPos)
        })

        appModel.isConnectable.observe(viewLifecycleOwner, Observer {
            connectableSwitch.isChecked = it
        })
        connectableSwitch.setOnCheckedChangeListener { _, isChecked ->
            appModel.isConnectable.postValue(isChecked)
        }

        appModel.advertiseDeviceName.observe(viewLifecycleOwner, Observer {
            advertiseDeviceNameSwitch.isChecked = it
        })
        advertiseDeviceNameSwitch.setOnCheckedChangeListener { _, isChecked ->
            appModel.advertiseDeviceName.postValue(isChecked)
        }

        advertiseStartBtn.setOnClickListener {
            appModel.startAdvertising()
        }

        advertiseStopBtn.setOnClickListener {
            appModel.stopAdvertising()
        }
        //endregion

        //region Connections
        val currentConnectionCountView: TextView = root.findViewById(R.id.connections_current_value)

        appModel.connectedDevices.observe(viewLifecycleOwner, Observer {
            currentConnectionCountView.text = it.size.toString()
        })
        //endregion

        //region External links
        root.findViewById<TextView>(R.id.home_sources_link).setOnClickListener {
            openWebsite(AppModel.sourcesLink)
        }

        root.findViewById<TextView>(R.id.home_developer_link).setOnClickListener {
            openWebsite(AppModel.developerLink)
        }

        return root
    }

    private fun setupDeviceSelector(deviceSelector: Spinner) {
        deviceSelectorAdapter = ArrayAdapter(
            activity as Context,
            R.layout.spinner_item_device,
            AppModel.supportedDevices.map { it.name }
        )
        deviceSelectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        deviceSelector.adapter = deviceSelectorAdapter
        deviceSelector.onItemSelectedListener = selectorListener
    }

    private fun updateSelectedDevice(selectedDeviceName: String) {
        val selectedDevice: Device? =
            AppModel.supportedDevices.find { selectedDeviceName == it.name }
        selectedDevice?.let {
            appModel.selectDevice(selectedDevice)
        }
    }

    private fun openWebsite(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}