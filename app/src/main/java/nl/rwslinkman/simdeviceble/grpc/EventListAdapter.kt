package nl.rwslinkman.simdeviceble.grpc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R
import java.text.SimpleDateFormat
import java.util.*

class EventListAdapter: RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    private val dataSet: MutableList<Item> = mutableListOf()

    data class Item(val data: String, val timestamp: Date = Date())

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateView: TextView = itemView.findViewById(R.id.item_grpc_event_timestamp)
        val eventView: TextView = itemView.findViewById(R.id.item_grpc_event_data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemRoot: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_grpc_event, parent, false)
        return EventViewHolder(itemRoot)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event: Item = dataSet[position]
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.getDefault())
        val eventDate: String = sdf.format(event.timestamp)

        holder.dateView.text = eventDate
        holder.eventView.text = event.data
    }

    fun addGrpcEvent(eventItem: Item) {
        dataSet.add(eventItem)
    }
}