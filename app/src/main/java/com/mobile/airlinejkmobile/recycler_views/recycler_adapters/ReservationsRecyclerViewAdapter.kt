package com.mobile.airlinejkmobile.recycler_views.recycler_adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Reservation

class ReservationsRecyclerViewAdapter(
    private var reservations: List<Reservation>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<ReservationsRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reservations_row, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val f = reservations[position]
        holder.route.text = ""+ f.id +" - "+f.flightInfo
        //holder.duration.text = "Duración: " + f.flight.duration

        holder.price.text = "Precio Total: " + f.totalPrice.toString()
       // holder.seats.text = "Cantidad de Asientos: " + f.seatQ.toString()

        holder.seats.text = "Cantidad de Asientos: " + f.seatQuantity.toString()
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(f)
        }

    }

    override fun getItemCount(): Int {
        return reservations.size
    }

    fun updateList(list: ArrayList<Reservation>){
        reservations = list
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val route: TextView = view.findViewById(R.id.flight)
        val price: TextView = view.findViewById(R.id.total_price)
        val seats: TextView = view.findViewById(R.id.reserved_seats)
    }

    interface ClickListener{
        fun onItemClick(res: Reservation)
    }

}