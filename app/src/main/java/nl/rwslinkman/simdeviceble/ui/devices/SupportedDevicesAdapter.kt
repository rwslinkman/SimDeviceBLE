package nl.rwslinkman.simdeviceble.ui.devices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.device.model.Device

class SupportedDevicesAdapter(private val dataSet: List<Device>): RecyclerView.Adapter<SupportedDevicesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.item_device_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemRoot: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_supported_device, parent, false)
        return ViewHolder(itemRoot)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deviceItem: Device = dataSet[position]

        // TODO Populate device item view
        holder.nameView.text = deviceItem.name
    }
}