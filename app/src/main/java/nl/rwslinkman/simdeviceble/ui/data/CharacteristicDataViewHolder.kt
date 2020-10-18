package nl.rwslinkman.simdeviceble.ui.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nl.rwslinkman.simdeviceble.R

internal class CharacteristicDataViewHolder(inflater: LayoutInflater) {
    val itemView: View = inflater.inflate(R.layout.list_item_characteristic, null)
    val nameView: TextView = itemView.findViewById(R.id.item_characteristic_name)
    val uuidView: TextView = itemView.findViewById(R.id.item_characteristic_uuid)
    val valueView: TextView = itemView.findViewById(R.id.item_characteristic_value)
    val updateBlock: ViewGroup = itemView.findViewById(R.id.item_characteristic_updates_block)
}