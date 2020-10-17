package nl.rwslinkman.simdeviceble.ui.devices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.device.model.Device

class SupportedDevicesAdapter(private val dataSet: List<Device>): RecyclerView.Adapter<SupportedDevicesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.item_device_name)
        val servicesView: LinearLayout = itemView.findViewById(R.id.item_device_services)
    }

    private class ServiceViewHolder(inflater: LayoutInflater) {
        val itemView: View = inflater.inflate(R.layout.list_item_servicedata, null)
        val nameView: TextView = itemView.findViewById(R.id.item_service_name)
        val uuidView: TextView = itemView.findViewById(R.id.item_service_uuid)
        val characteristicsBlock: LinearLayout = itemView.findViewById(R.id.item_service_characteristics)
    }

    private class CharacteristicViewHolder(inflater: LayoutInflater) {
        val itemView: View = inflater.inflate(R.layout.list_item_supported_characteristic, null)
        val nameView: TextView = itemView.findViewById(R.id.item_characteristic_name)
        val uuidView: TextView = itemView.findViewById(R.id.item_characteristic_uuid)
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

        holder.nameView.text = deviceItem.name

        val inflater = LayoutInflater.from(holder.itemView.context)
        deviceItem.services.forEach { serviceItem ->
            val serviceViewHolder = ServiceViewHolder(inflater)

            serviceViewHolder.nameView.text = serviceItem.name
            serviceViewHolder.uuidView.text = serviceItem.uuid.toString()

            serviceItem.characteristics.forEach {
                val charViewHolder = CharacteristicViewHolder(inflater)

                charViewHolder.nameView.text = it.name
                charViewHolder.uuidView.text = it.uuid.toString()

                serviceViewHolder.characteristicsBlock.addView(charViewHolder.itemView)
            }

            holder.servicesView.addView(serviceViewHolder.itemView)
        }
    }
}