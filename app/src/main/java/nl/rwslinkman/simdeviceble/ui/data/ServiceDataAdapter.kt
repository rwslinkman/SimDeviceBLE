package nl.rwslinkman.simdeviceble.ui.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.device.model.Service

class ServiceDataAdapter: RecyclerView.Adapter<ServiceDataAdapter.ViewHolder>() {

    private val dataSet: MutableList<Service> = mutableListOf()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.item_service_name)
        val uuidView: TextView = itemView.findViewById(R.id.item_service_uuid)
        val characteristicsBlock: LinearLayout = itemView.findViewById(R.id.item_service_characteristics)
    }

    class CharacteristicViewHolder(inflater: LayoutInflater) {
        val itemView: View = inflater.inflate(R.layout.list_item_characteristic, null)
        val nameView: TextView = itemView.findViewById(R.id.item_characteristic_name)
        val uuidView: TextView = itemView.findViewById(R.id.item_characteristic_uuid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemRoot: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_servicedata, parent, false)
        return ViewHolder(itemRoot)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serviceItem: Service = dataSet[position]

        holder.nameView.text = serviceItem.name
        holder.uuidView.text = serviceItem.uuid.toString()

        val inflater = LayoutInflater.from(holder.itemView.context)
        serviceItem.characteristics.forEach {
            // Dynamically add child views
            val charViewHolder = CharacteristicViewHolder(inflater)
            charViewHolder.nameView.text = it.name
            charViewHolder.uuidView.text = it.uuid.toString()

            holder.characteristicsBlock.addView(charViewHolder.itemView)
        }
    }

    fun updateDataSet(dataSet: List<Service>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        this.notifyDataSetChanged()
    }
}