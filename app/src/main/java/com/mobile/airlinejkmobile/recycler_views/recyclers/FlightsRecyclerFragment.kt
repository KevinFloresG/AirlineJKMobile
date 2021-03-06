package com.mobile.airlinejkmobile.recycler_views.recyclers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Flight
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.fragments.FlightDetailFragment
import com.mobile.airlinejkmobile.fragments.ReservationAddFragment
import com.mobile.airlinejkmobile.recycler_views.recycler_adapters.FlightsRecyclerViewAdapter
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.io.Serializable
import java.net.URI
import java.net.URISyntaxException

class FlightsRecyclerFragment : Fragment(), FlightsRecyclerViewAdapter.ClickListener {

    private lateinit var adapter: FlightsRecyclerViewAdapter
    private lateinit var recycler : RecyclerView
    private var flights = ArrayList<Flight>()
    private var ws: WebSocketClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flights_recycler, container, false)
        recycler = view.findViewById(R.id.recycler_flights)
        initRecycler(view)
        connectWebSocket()
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
            route = it.route.id
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

    private fun connectWebSocket() {
        val uri: URI
        try {
            uri = URI("ws://${Model.SERVER_IP}:8088/AirlineJK/flight")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        ws = object : WebSocketClient(uri), Serializable {
            override fun onMessage(s: String) {
                val obj = JSONObject(s)
                val content = obj.getString("content")
                when(obj.get("type")){
                    "all" -> {
                        flights = ArrayList(Klaxon().parseArray<Flight>(content))
                        activity?.runOnUiThread {
                            adapter.updateList(flights)
                        }
                    }
                    "update" -> {
                        val f = Klaxon().parse<Flight>(content)!!
                        loop@ for (x in 0..flights.size){
                            if (flights[x].id == f.id){
                                flights[x] = f
                                activity?.runOnUiThread {
                                    adapter.notifyItemChanged(x)
                                }
                                break@loop
                            }
                        }
                    }
                    "delete" -> {
                        val f = Klaxon().parse<Flight>(content)!!
                        loop@ for (x in 0..flights.size){
                            if (flights[x].id == f.id){
                                flights.removeAt(x)
                                activity?.runOnUiThread {
                                    adapter.notifyItemRemoved(x)
                                }
                                break@loop
                            }
                        }
                    }
                    "add" -> {
                        val f = Klaxon().parse<Flight>(content)!!
                        flights.add(0,f)
                        activity?.runOnUiThread {
                            adapter.notifyItemInserted(0)
                            recycler.scrollToPosition(0)
                        }
                    }
                }
            }
            override fun onClose(i: Int, s: String, b: Boolean) {}
            override fun onError(e: Exception) {}
            override fun onOpen(serverHandshake: ServerHandshake) {}
        }
        (ws as WebSocketClient).connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        ws?.close()
    }

}