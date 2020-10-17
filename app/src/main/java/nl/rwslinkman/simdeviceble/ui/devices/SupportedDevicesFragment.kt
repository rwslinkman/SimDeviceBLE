package nl.rwslinkman.simdeviceble.ui.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.AppModel
import nl.rwslinkman.simdeviceble.R

class SupportedDevicesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_supported_devices, container, false)

        root.findViewById<RecyclerView>(R.id.supported_devices_list).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = SupportedDevicesAdapter(AppModel.supportedDevices)
        }

        return root
    }
}