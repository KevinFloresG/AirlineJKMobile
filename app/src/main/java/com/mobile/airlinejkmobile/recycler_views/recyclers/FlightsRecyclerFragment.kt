package com.mobile.airlinejkmobile.recycler_views.recyclers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Flight
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.fragments.FlightDetailFragment
import com.mobile.airlinejkmobile.fragments.ReservationAddFragment
import com.mobile.airlinejkmobile.recycler_views.recycler_adapters.FlightsRecyclerViewAdapter

class FlightsRecyclerFragment : Fragment(), FlightsRecyclerViewAdapter.ClickListener {

    private lateinit var adapter: FlightsRecyclerViewAdapter
    private val flights: ArrayList<Flight> = ArrayList(Model.flights.values)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flights_recycler, container, false)
        initRecycler(view)
        return view
    }

    private fun initRecycler(view: View){
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_flights)
        recycler.layoutManager = LinearLayoutManager(activity)
        adapter = FlightsRecyclerViewAdapter(flights, this)
        recycler.adapter = adapter

        val search = view.findViewById<EditText>(R.id.search_flights)
        search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })
        //Code for Swiping on a Recycler Row
        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { return false }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                displayDetailFragment(viewHolder)
            }
        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(recycler)
    }

    private fun displayDetailFragment(viewHolder: RecyclerView.ViewHolder){
        val fragment: Fragment = ReservationAddFragment.newInstance(flights[viewHolder.adapterPosition])
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.hide(activity?.supportFragmentManager!!.findFragmentByTag("recycler_flights")!!)
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        adapter.notifyItemChanged(viewHolder.adapterPosition)
    }

    private fun filter(string: String){
        val filteredFlights = ArrayList<Flight>()
        var route : String
        flights.forEach{
            route = it.start + " - " + it.end
            if (route.lowercase().contains(string.lowercase())){
                filteredFlights.add(it)
            }
        }
        adapter.updateList(filteredFlights)
    }

    override fun onItemClick(flight: Flight) {
        val fragment: Fragment = FlightDetailFragment.newInstance(flight)
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.hide(activity?.supportFragmentManager!!.findFragmentByTag("recycler_flights")!!)
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}