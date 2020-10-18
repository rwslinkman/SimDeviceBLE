package nl.rwslinkman.simdeviceble.ui.data

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import nl.rwslinkman.simdeviceble.ui.data.controls.CharacteristicControls
import nl.rwslinkman.simdeviceble.ui.data.controls.NumberCharacteristicControls

class ServiceDataAdapter(private val listener: CharacteristicManipulationListener): RecyclerView.Adapter<ServiceDataViewHolder>() {

    interface CharacteristicManipulationListener {
        fun setCharacteristicValue(characteristic: Characteristic, setValue: Editable)

        fun notifyCharacteristic(characteristic: Characteristic)
    }

    private val dataSet: MutableList<Service> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceDataViewHolder {
        val itemRoot: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_servicedata, parent, false)
        return ServiceDataViewHolder(itemRoot)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ServiceDataViewHolder, position: Int) {
        val serviceItem: Service = dataSet[position]

        holder.nameView.text = serviceItem.name
        holder.uuidView.text = serviceItem.uuid.toString()

        val inflater = LayoutInflater.from(holder.itemView.context)
        serviceItem.characteristics.forEach { charItem ->
            // Dynamically add child views
            val charViewHolder = CharacteristicDataViewHolder(inflater)
            charViewHolder.nameView.text = charItem.name
            charViewHolder.uuidView.text = charItem.uuid.toString()

            var updateControls: CharacteristicControls? = null
            // TODO CHose updateControls type based on characteristic
            if (charItem.isRead) {
                updateControls = NumberCharacteristicControls()
            }

            updateControls?.let {
                val controlsView: View = inflater.inflate(it.controlsLayoutId, charViewHolder.updateBlock)
                it.setup(controlsView)

                it.bind(charItem, listener)
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