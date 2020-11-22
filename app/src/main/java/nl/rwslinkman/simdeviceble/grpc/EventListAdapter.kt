package nl.rwslinkman.simdeviceble.grpc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.rwslinkman.simdeviceble.R

class EventListAdapter: RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    private val dataSet: MutableList<String> = mutableListOf()

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventView: TextView = itemView.findViewById(R.id.item_grpc_event)
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
        val event: String = dataSet[position]
        holder.eventView.text = event
    }

    fun addGrpcEvent(event: String) {
        dataSet.add(event)
    }
}