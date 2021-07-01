package com.mobile.airlinejkmobile.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Flight

private const val START = "start"
private const val END = "end"
private const val HOUR = "hour"
private const val ROUTE = "route"
private const val DATE = "date"
private const val DURATION = "duration"
private const val PRICE = "price"
private const val DISCOUNT = "discount"
private const val AVAILABLE_SEATS = "availableSeats"

class FlightDetailFragment : Fragment() {

    //private var start: String? = null
    //private var end: String? = null
    private var duration: String? = null
    private var hour: String? = null
    private var date: String? = null
    private var price: Double? = null
    private var discount: Double? = null
    private var availableSeats: Int? = null
    private var route: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            /*start = it.getString(START)
            end = it.getString(END)*/
            hour = it.getString(HOUR)
            route = it.getString(ROUTE)
            date = it.getString(DATE)
            duration = it.getString(DURATION)
            price = it.getDouble(PRICE)
            discount = it.getDouble(DISCOUNT)
            availableSeats = it.getInt(AVAILABLE_SEATS)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flight_detail, container, false)
        view.findViewById<TextView>(R.id.route_txt).text = route
        view.findViewById<TextView>(R.id.discount_txt).text = discount.toString() + "%"
        view.findViewById<TextView>(R.id.duration_txt).text = duration
        view.findViewById<TextView>(R.id.date_txt).text = date
        view.findViewById<TextView>(R.id.hour_txt).text = hour
        view.findViewById<TextView>(R.id.price_txt).text = price.toString()
        view.findViewById<TextView>(R.id.seats_txt).text = availableSeats.toString()
        view.findViewById<Button>(R.id.add_btn).setOnClickListener {
            activity?.onBackPressed()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(flight: Flight) =
            FlightDetailFragment().apply {
                arguments = Bundle().apply {
                    //putString(START, flight)
                    //putString(END, flight.end)
                    putString(HOUR, flight.route.schedule.departureTime)
                    putString(ROUTE, flight.route.id)
                    putString(DATE, flight.departureDate.split("T")[0])
                    putString(DURATION, "${flight.route.durationhours}:${flight.route.durationminutes}")
                    putDouble(PRICE, flight.price)
                    putDouble(DISCOUNT, flight.discount * 100)
                    putInt(AVAILABLE_SEATS, flight.availableSeats)
                }
            }
    }
}