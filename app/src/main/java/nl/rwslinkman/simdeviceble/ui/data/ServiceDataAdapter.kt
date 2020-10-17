package nl.rwslinkman.simdeviceble.ui.data

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service

class ServiceDataAdapter(private val listener: CharacteristicManipulationListener): RecyclerView.Adapter<ServiceDataAdapter.ViewHolder>() {

    interface CharacteristicManipulationListener {
        fun setCharacteristicValue(characteristic: Characteristic, setValue: Editable)

        fun notifyCharacteristic(characteristic: Characteristic)
    }

    private val dataSet: MutableList<Service> = mutableListOf()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.item_service_name)
        val uuidView: TextView = itemView.findViewById(R.id.item_service_uuid)
        val characteristicsBlock: ViewGroup = itemView.findViewById(R.id.item_service_characteristics)
    }

    private class CharacteristicViewHolder(inflater: LayoutInflater) {
        val itemView: View = inflater.inflate(R.layout.list_item_characteristic, null)
        val nameView: TextView = itemView.findViewById(R.id.item_characteristic_name)
        val uuidView: TextView = itemView.findViewById(R.id.item_characteristic_uuid)
        val valueView: TextView = itemView.findViewById(R.id.item_characteristic_value)
        val updateBlock: ViewGroup = itemView.findViewById(R.id.item_characteristic_updates_block)
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
        serviceItem.characteristics.forEach { charItem ->
            // Dynamically add child views
            val charViewHolder = CharacteristicViewHolder(inflater)
            charViewHolder.nameView.text = charItem.name
            charViewHolder.uuidView.text = charItem.uuid.toString()

            if (charItem.isRead) {
                val updateControls: View
                // TODO: Add more types of update controls
                val numberControlsLayout = R.layout.characteristic_update_control_number
                updateControls = inflater.inflate(numberControlsLayout, charViewHolder.updateBlock)
                val valueControl: EditText = updateControls.findViewById(R.id.characterstic_control_edittext)

                updateControls.findViewById<Button>(R.id.characteristic_control_set_btn).setOnClickListener {
                    val fieldValue: Editable = valueControl.text
                    listener.setCharacteristicValue(charItem, fieldValue)
                }
                val notifyBtn = updateControls.findViewById<Button>(R.id.characteristic_control_notify_btn)
                notifyBtn.isEnabled = charItem.isNotify
                notifyBtn.setOnClickListener {
                    listener.notifyCharacteristic(charItem)
                }
            }

            holder.characteristicsBlock.addView(charViewHolder.itemView)
        }
    }

    fun updateDataSet(dataSet: List<Service>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        this.notifyDataSetChanged()
    }
}