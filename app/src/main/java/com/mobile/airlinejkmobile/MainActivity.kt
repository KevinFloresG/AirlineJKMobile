package com.mobile.airlinejkmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.mobile.airlinejkmobile.business_logic.User
import com.mobile.airlinejkmobile.databinding.ActivityMainBinding
import com.mobile.airlinejkmobile.fragments.*
import com.mobile.airlinejkmobile.recycler_views.recyclers.FlightsRecyclerFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        val bundle = intent.extras
        val user =  bundle!!.getSerializable("User") as User

        Toast.makeText(
            this,
            "Bienvenid@ "+user.name+" "+user.lastName+".",
            Toast.LENGTH_SHORT
        ).show()
        */

        setSupportActionBar(binding.content.toolbar)
        val toggle = ActionBarDrawerToggle(this,
            binding.drawerLayout , binding.content.toolbar,
            R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navMenu.setNavigationItemSelectedListener(this)

        //changeFragment("Vuelos en Oferta", FlightsOfferFragment())*/

        //=============== TRY RECYCLER ========================
        //val fragment : Fragment = RecyclerTryFragment.newInstance()
        //val transaction = supportFragmentManager.beginTransaction()
        //transaction.replace(R.id.fragment_container, FlightsRecyclerFragment(),"recycler").commit()
        //=====================================================

        changeFragment("Vuelos", FlightsRecyclerFragment(), "recycler_flights")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        when(item.itemId){
            R.id.profile -> changeFragment("Perfil", ProfileFragment(), "")
            R.id.flights_offer -> changeFragment("Vuelos en Oferta", FlightsOfferFragment(),"")
            R.id.flights -> changeFragment("Vuelos", FlightsRecyclerFragment(), "recycler_flights")
            R.id.reservations ->changeFragment("Reservaciones", ReservationsFragment(),"")
            R.id.checkIn -> changeFragment("Check In", CheckInFragment(),"")
            else -> return false
        }
        return true
    }

    private fun changeFragment(title : String, frag : Fragment, tag : String){
        supportActionBar?.title = title
        val fragment = supportFragmentManager.beginTransaction()
        if (tag.isEmpty())
            fragment.replace(R.id.fragment_container, frag)
        else
            fragment.replace(R.id.fragment_container, frag, tag)
        fragment.commit()
    }

}