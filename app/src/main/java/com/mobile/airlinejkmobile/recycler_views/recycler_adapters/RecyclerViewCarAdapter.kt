package com.mobile.airlinejkmobile.recycler_views.recycler_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.airlinejkmobile.business_logic.Car
import com.mobile.airlinejkmobile.R

class RecyclerViewCarAdapter(
    private val listCars: List<Car>,
    private val clickListener: ClickListener
    ) : RecyclerView.Adapter<RecyclerViewCarAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.recycler_car_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCars.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.carTitleTextview.text = listCars[position].title
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(listCars[position])
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val carTitleTextview: TextView = view.findViewById(R.id.car_title)
    }

    interface ClickListener{
        fun onItemClick(car: Car)
    }

}