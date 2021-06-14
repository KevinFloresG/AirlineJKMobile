package com.mobile.airlinejkmobile.recycler_views.recycler_adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Flight

class FlightsRecyclerViewAdapter(
    private var flights: List<Flight>,
    private val clickListener: ClickListener
    ) : RecyclerView.Adapter<FlightsRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.flights_row, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val f = flights[position]
        holder.route.text = f.start + " - " + f.end
        holder.duration.text = "Duración: " + f.duration
        holder.price.text = "Precio: " + f.price.toString()
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(f)
        }
    }

    override fun getItemCount(): Int {
        return flights.size
    }

    fun updateList(list: ArrayList<Flight>){
        flights = list
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val route: TextView = view.findViewById(R.id.flight_route)
        val duration: TextView = view.findViewById(R.id.flight_duration)
        val price: TextView = view.findViewById(R.id.flight_price)
    }

    interface ClickListener{
        fun onItemClick(flight: Flight)
    }

}