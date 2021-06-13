package com.mobile.airlinejkmobile.recycler_views.recyclers

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.airlinejkmobile.business_logic.Car
import com.mobile.airlinejkmobile.fragments.DetailCarFragment
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.recycler_views.recycler_adapters.RecyclerViewCarAdapter

class RecyclerTryFragment : Fragment(), RecyclerViewCarAdapter.ClickListener {

    private lateinit var adapter: RecyclerViewCarAdapter
    private val listCars: ArrayList<Car> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_try, container, false)
        initRecycler(view)
        return view
    }

    private fun initRecycler(view: View){
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_try)
        buildDisplayData()
        recycler.layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerViewCarAdapter(listCars, this)
        recycler.adapter = adapter

        //Code for Swiping on a Recycler Row
        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

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
            listCars.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        builder.setNegativeButton("Cancelar"){ _, _ ->
            adapter.notifyItemChanged(viewHolder.adapterPosition)
        }
        builder.show()
    }

    private fun buildDisplayData(){
        listCars.add(Car("Car 1"))
        listCars.add(Car("Car 2"))
        listCars.add(Car("Car 3"))
        listCars.add(Car("Car 4"))
        listCars.add(Car("Car 5"))
    }

    override fun onItemClick(car: Car) {
        val fragment: Fragment = DetailCarFragment.newInstance(car.title!!)
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.hide(activity?.supportFragmentManager!!.findFragmentByTag("recycler")!!)
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}