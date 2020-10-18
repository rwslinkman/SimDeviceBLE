package nl.rwslinkman.simdeviceble.ui.data

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R

class ServiceDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val nameView: TextView = itemView.findViewById(R.id.item_service_name)
    val uuidView: TextView = itemView.findViewById(R.id.item_service_uuid)
    val characteristicsBlock: ViewGroup = itemView.findViewById(R.id.item_service_characteristics)
}