package com.mobile.airlinejkmobile.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.business_logic.Reservation
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CheckInFragment : Fragment() {

    private lateinit var idReservation : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_check_in, container, false)
        idReservation = view.findViewById(R.id.idReservation)
        view.findViewById<Button>(R.id.btnSearch).setOnClickListener {
            look()
        }
        return view
    }

    private fun look(){
        if (idReservation.text.isBlank()){
            Toast.makeText(context, "Ingresa un Id", Toast.LENGTH_SHORT).show()
            return
        }
        val cantInt = idReservation.text.toString().toInt()
        val result = getReservationByID(cantInt)

        val fragment: Fragment = EndCheckInFragment.newInstance(
            result!!.getInt("id"),
            result!!.getInt("flightId"),
            result!!.getInt("seatQuantity")
        )
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.hide(this)
        transaction.add(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun getReservationByID(id: Int): JSONObject? {
        var json: JSONObject? = null
        val thread = Thread {
            var apiUrl = "http://"+Model.SERVER_IP+":8088/AirlineJK/reservations/get"
            var current = ""

            val url: URL
            var urlConnection: HttpURLConnection? = null
            apiUrl += "?id=$id"
            try {
                url = URL(apiUrl)
                urlConnection = url
                    .openConnection() as HttpURLConnection

                urlConnection.requestMethod = "GET"
                urlConnection.doOutput = true

                urlConnection.setRequestProperty("Content-type", "application/json")
                val outS = urlConnection.outputStream
                val dOS = DataOutputStream(outS)
                dOS.flush()
                dOS.close()
                val `in` = urlConnection.inputStream
                val isw = InputStreamReader(`in`)
                var data = isw.read()
                while (data != -1) {
                    current += data.toChar()
                    data = isw.read()
                    print(current)
                }
                json = JSONObject(current)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
        return json
    }

}