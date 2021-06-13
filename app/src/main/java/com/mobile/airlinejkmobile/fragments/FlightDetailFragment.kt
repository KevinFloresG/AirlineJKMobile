package com.mobile.airlinejkmobile.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Flight

private const val START = "start"
private const val END = "end"
private const val DURATION = "duration"
private const val PRICE = "price"

class FlightDetailFragment : Fragment() {

    private var start: String? = null
    private var end: String? = null
    private var duration: String? = null
    private var price: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            start = it.getString(START)
            end = it.getString(END)
            duration = it.getString(DURATION)
            price = it.getDouble(PRICE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flight_detail, container, false)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(flight: Flight) =
            FlightDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(START, flight.start)
                    putString(END, flight.end)
                    putString(DURATION, flight.duration)
                    putDouble(PRICE, flight.price)
                }
            }
    }
}