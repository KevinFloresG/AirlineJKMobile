package com.mobile.airlinejkmobile.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Flight
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.business_logic.Reservation
import com.mobile.airlinejkmobile.business_logic.User

private const val ID = "id"
private const val START = "start"
private const val END = "end"
private const val HOUR = "hour"
private const val DATE = "date"
private const val DURATION = "duration"
private const val PRICE = "price"
private const val DISCOUNT = "discount"
private const val AVAILABLE_SEATS = "availableSeats"

class ReservationAddFragment : Fragment() {

    private var idFlight : Int? = null
    private var start: String? = null
    private var end: String? = null
    private var duration: String? = null
    private var hour: String? = null
    private var date: String? = null
    private var price: Double? = null
    private var discount: Double? = null
    private var availableSeats: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idFlight = it.getInt(ID)
            start = it.getString(START)
            end = it.getString(END)
            hour = it.getString(HOUR)
            date = it.getString(DATE)
            duration = it.getString(DURATION)
            price = it.getDouble(PRICE)
            discount = it.getDouble(DISCOUNT)
            availableSeats = it.getInt(AVAILABLE_SEATS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservation_add, container, false)
        view.findViewById<TextView>(R.id.route_txt).text = "$start - $end"
        view.findViewById<TextView>(R.id.discount_txt).text = discount.toString()
        view.findViewById<TextView>(R.id.duration_txt).text = duration
        view.findViewById<TextView>(R.id.hour_txt).text = hour
        view.findViewById<TextView>(R.id.date_txt).text = date
        view.findViewById<TextView>(R.id.price_txt).text = price.toString()
        view.findViewById<TextView>(R.id.seats_txt).text = availableSeats.toString()
        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener {
            activity?.onBackPressed()
        }
        view.findViewById<Button>(R.id.add_btn).setOnClickListener {
            addReservation(view)
        }
        return view
    }

    private fun addReservation(view: View){
        val cantSeats = view.findViewById<TextView>(R.id.cantSeats).text
        if (cantSeats.isBlank()){
            Toast.makeText(context, "Ingresa una Cant. de Asientos", Toast.LENGTH_SHORT).show()
            return
        }
        val cantInt = cantSeats.toString().toInt()
        if (cantInt > availableSeats!!) {
            Toast.makeText(context, "No hay tantos Asientos Disponibles!", Toast.LENGTH_SHORT).show()
            return
        }
        if (cantInt <= 0){
            Toast.makeText(context, "Ingresa una Cant. de Asientos Correcta", Toast.LENGTH_SHORT).show()
            return
        }
        var f = Model.flights[idFlight]!!
        var u = Model.currentUser!!
        Model.reservations.add(
            Reservation(++Model.reservationId, f, cantInt, 0, u, cantInt * price!!)
        )
        f.availableSeats -= cantInt
        Toast.makeText(context, "Reservación Correcta", Toast.LENGTH_LONG).show()
        activity?.onBackPressed()
    }

    companion object {
        @JvmStatic
        fun newInstance(flight: Flight) =
            ReservationAddFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID, flight.id)
                    putString(START, flight.start)
                    putString(END, flight.end)
                    putString(HOUR, flight.hour)
                    putString(DATE, flight.date)
                    putString(DURATION, flight.duration)
                    putDouble(PRICE, flight.price)
                    putDouble(DISCOUNT, flight.discount)
                    putInt(AVAILABLE_SEATS, flight.availableSeats)
                }
            }
    }
}