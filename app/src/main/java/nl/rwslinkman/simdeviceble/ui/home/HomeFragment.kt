package nl.rwslinkman.simdeviceble.ui.home

import android.content.Context
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val bleSupportedTextView: TextView = root.findViewById(R.id.ble_status_supported_value)
        val bleEnabledTextView: TextView = root.findViewById(R.id.ble_status_enabled_value)
        val bleAdvertisingSupportedTextView: TextView =
            root.findViewById(R.id.ble_status_advertising_value)
        val enableBluetoothBtn = root.findViewById<Button>(R.id.ble_enable_btn)

        appModel.bluetoothSupported.observe(this, Observer {
            bleSupportedTextView.text = if (it) "Yes" else "No"
        })

        appModel.bluetoothEnabled.observe(this, Observer {
            bleEnabledTextView.text = if (it) "Yes" else "No"

        })

        appModel.bluetoothAdvertisingSupported.observe(this, Observer {
            bleAdvertisingSupportedTextView.text = if (it) "Yes" else "No"
            enableBluetoothBtn.isEnabled = !it
        })

        enableBluetoothBtn.setOnClickListener {
            appModel.enableBluetooth()
        }

        // Advertising
        val advertisingEnabledView: TextView = root.findViewById(R.id.advertising_enabled_value)
        val advertisementNameView: TextView = root.findViewById(R.id.advertising_name_value)
        val connectableSwitch: SwitchCompat = root.findViewById(R.id.advertising_connectable_value)
        val advertiseDeviceNameSwitch: SwitchCompat = root.findViewById(R.id.advertising_allowname_value)
        val advertiseStartBtn = root.findViewById<Button>(R.id.advertising_start_btn)
        val advertiseStopBtn = root.findViewById<Button>(R.id.advertising_stop_btn)

        appModel.isAdvertising.observe(this, Observer {
            advertisingEnabledView.text = if (it) "Yes" else "No"
            advertiseStartBtn.isEnabled = !it
            advertiseStopBtn.isEnabled = it
        })

        appModel.advertisementName.observe(this, Observer {
            advertisementNameView.text = it
        })

        val deviceSelector: Spinner = root.findViewById(R.id.advertising_device_value)
        setupDeviceSelector(deviceSelector)

        appModel.isConnectable.observe(this, Observer {
            connectableSwitch.isChecked = it
        })
        connectableSwitch.setOnCheckedChangeListener { _, isChecked ->
            appModel.isConnectable.postValue(isChecked)
        }

        appModel.advertiseDeviceName.observe(this, Observer {
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

        return root
    }

    private fun setupDeviceSelector(deviceSelector: Spinner) {
        val adapter = ArrayAdapter(
            activity as Context,
            R.layout.spinner_item_device,
            AppModel.supportedDevices.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        deviceSelector.adapter = adapter
        deviceSelector.onItemSelectedListener = selectorListener
    }

    private fun updateSelectedDevice(selectedDeviceName: String) {
        val selectedDevice: Device? = AppModel.supportedDevices.find { selectedDeviceName == it.name }
        selectedDevice?.let {
            appModel.selectDevice(selectedDevice)
        }
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}