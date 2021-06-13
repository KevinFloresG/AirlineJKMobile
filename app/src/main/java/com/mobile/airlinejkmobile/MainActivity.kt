package com.mobile.airlinejkmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.mobile.airlinejkmobile.databinding.ActivityMainBinding
import com.mobile.airlinejkmobile.fragments.*
import com.mobile.airlinejkmobile.recycler_views.recyclers.RecyclerTryFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, RecyclerTryFragment(),"recycler").commit()
        //=====================================================
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        when(item.itemId){
            R.id.profile -> changeFragment("Perfil", ProfileFragment())
            R.id.flights_offer -> changeFragment("Vuelos en Oferta", FlightsOfferFragment())
            R.id.flights -> changeFragment("Vuelos", FlightsFragment())
            R.id.reservations ->changeFragment("Reservaciones", ReservationsFragment())
            R.id.checkIn -> changeFragment("Check In", CheckInFragment())
            else -> return false
        }
        return true
    }

    private fun changeFragment(title : String, frag : Fragment){
        supportActionBar?.title = title
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_container, frag).commit()
    }

}