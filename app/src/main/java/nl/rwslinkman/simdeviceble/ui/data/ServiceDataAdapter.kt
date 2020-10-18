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
import java.util.*

class ServiceDataAdapter(private val listener: CharacteristicManipulationListener): RecyclerView.Adapter<ServiceDataViewHolder>() {

    interface CharacteristicManipulationListener {
        fun setCharacteristicValue(characteristic: Characteristic, setValue: Editable)

        fun notifyCharacteristic(characteristic: Characteristic)
    }

    private val dataSet: MutableList<Service> = mutableListOf()
    private val dataContainer: MutableMap<UUID, String> = mutableMapOf()

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

        holder.characteristicsBlock.removeAllViews()
        val inflater = LayoutInflater.from(holder.itemView.context)
        val characteristics = serviceItem.characteristics
        characteristics.forEach { charItem ->
            // Dynamically add child views
            val charViewHolder = CharacteristicDataViewHolder(inflater)
            charViewHolder.nameView.text = charItem.name
            charViewHolder.uuidView.text = charItem.uuid.toString()

            val charValue: String = dataContainer[charItem.uuid] ?: "n/a"
            charViewHolder.valueView.text = charValue

            var updateControls: CharacteristicControls? = null
            // TODO Chose updateControls type based on characteristic
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

    fun updateDataSet(dataSet: List<Service>, dataContainer: Map<UUID, String>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        this.dataContainer.clear()
        this.dataContainer.putAll(dataContainer)
        this.notifyDataSetChanged()
    }
}