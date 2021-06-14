package com.mobile.airlinejkmobile.recycler_views.recyclers

import android.app.AlertDialog
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
import com.mobile.airlinejkmobile.business_logic.Reservation
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.recycler_views.recycler_adapters.ReservationsRecyclerViewAdapter

class ReservationsRecyclerView : Fragment(){

    private lateinit var adapter: ReservationsRecyclerViewAdapter
    private val reservations: ArrayList<Reservation> = Model.reservations

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservations_recycler, container, false)
        initRecycler(view)
        return view
    }

    private fun initRecycler(view: View){
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_reservations)
        recycler.layoutManager = LinearLayoutManager(activity)
        adapter = ReservationsRecyclerViewAdapter(reservations)
        recycler.adapter = adapter

        val search = view.findViewById<EditText>(R.id.search_reservations)
        search.addTextChangedListener(object : TextWatcher {
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
                showDialog(viewHolder)
            }
        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(recycler)
    }

    private fun showDialog(viewHolder: RecyclerView.ViewHolder){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Eliminar Reservación")
        builder.setMessage("¿Está seguro de eliminar esta reservación?")
        builder.setPositiveButton("Confirmar"){ _, _ ->
            val position = viewHolder.adapterPosition
            reservations.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        builder.setNegativeButton("Cancelar"){ _, _ ->
            adapter.notifyItemChanged(viewHolder.adapterPosition)
        }
        builder.show()
    }

    private fun filter(string: String){
        val filteredReservations = ArrayList<Reservation>()
        var route : String
        reservations.forEach{
            route = it.flight.start + " - " + it.flight.end
            if (route.lowercase().contains(string.lowercase())){
                filteredReservations.add(it)
            }
        }
        adapter.updateList(filteredReservations)
    }

}