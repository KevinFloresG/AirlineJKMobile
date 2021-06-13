package com.mobile.airlinejkmobile.recycler_views.recycler_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Car

class FlightsRecyclerViewAdapter(
    private val flights: List<Car>,
    private val clickListener: ClickListener
    ) : RecyclerView.Adapter<FlightsRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_car_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.carTitleTextview.text = flights[position].title
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(flights[position])
        }
    }

    override fun getItemCount(): Int {
        return flights.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
       // val carTitleTextview: TextView = view.findViewById(R.id.car_title)
    }

    interface ClickListener{
        fun onItemClick(car: Car)
    }

}