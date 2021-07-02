package com.mobile.airlinejkmobile.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Flight
import com.mobile.airlinejkmobile.business_logic.Model
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONArray
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URI
import java.net.URISyntaxException
import java.net.URL

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class EndCheckInFragment : Fragment() {

    private var idR: Int? = null
    private var idF: Int? = null
    private var sQ: Int? = null
    private var ws: WebSocketClient? = null
    private var jsonArr = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idR = it.getInt(ARG_PARAM1)
            idF = it.getInt(ARG_PARAM2)
            sQ = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_end_check_in, container, false)
        printSeats(view, getTicketsByFlight(idF!!))
        connectWebSocket(view)
        view.findViewById<Button>(R.id.checkInBtn).setOnClickListener { finish() }
        return view
    }

    private fun finish(){
        ws?.send(jsonArr.toString())
        Toast.makeText(context, "Realizado", Toast.LENGTH_LONG).show()
        activity?.onBackPressed()
    }

    private fun printSeats(view : View, tickets : JSONArray?){
        data class Ticket(var rowN : Int, var columnN : Int)
        val list = Klaxon().parseArray<Ticket>(tickets.toString())
        val table = view.findViewById<TableLayout>(R.id.tlayt)
        var tr : TableRow
        for(t in list!!){
            tr = table.getChildAt(t.rowN) as TableRow
            var tc : TextView = tr.getChildAt(t.columnN) as TextView
            tc.text = "X"
        }
        for(i in 0..5){
            tr = table.getChildAt(i) as TableRow
            for(x in 0..5){
                var tc : TextView = tr.getChildAt(x) as TextView
                if(tc.text == "X")
                    tc.setOnClickListener {
                        Toast.makeText(context, "Asiento Ocupado", Toast.LENGTH_LONG).show()
                    }
                else
                    tc.setOnClickListener { selectSeat(tc, tc.text) }
            }
        }
    }

    fun selectSeat(tc : TextView, seat : CharSequence){

        tc.setBackgroundColor(Color.parseColor("#2F9F9B"))
        val r = seat[0].digitToInt()
        val c = seat[1].digitToInt()

        val reservation = JSONObject()
        reservation.put("id",idR)

        val flight = JSONObject()
        flight.put("id", idF)

        val ticket = JSONObject()
        ticket.put("rowN", r)
        ticket.put("columnN", c)
        ticket.put("owner", Model.currentUser?.name)
        ticket.put("flight", flight)
        ticket.put("reservation", reservation)

        val tickets = JSONArray()
        tickets.put(ticket)

        val wrapper = JSONObject()
        wrapper.put("type", "checkin")
        wrapper.put("content", tickets.toString())

        jsonArr = wrapper
        Log.d("JSONA",jsonArr.toString())
    }

    fun getTicketsByFlight(id: Int) : JSONArray?{
        var json: JSONArray? = null
        val thread = Thread {
            var apiUrl = "http:/${Model.SERVER_IP}:8088/AirlineJK/tickets/get/flight"
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

                json = JSONArray(current)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
        return json
    }

    private fun connectWebSocket(view : View) {
        val uri: URI
        try {
            uri = URI("ws://${Model.SERVER_IP}:8088/AirlineJK/ticket")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        ws = object : WebSocketClient(uri), Serializable {
            override fun onMessage(s: String) {
                val obj = JSONObject(s)
                val content = obj.getString("content")
                when(obj.get("type")){
                    "checkin" -> {
                        data class Ticket(var rowN : Int, var columnN : Int)
                        val list = Klaxon().parseArray<Ticket>(content)
                        val table = view.findViewById<TableLayout>(R.id.tlayt)
                        var tr : TableRow
                        for(t in list!!){
                            activity?.runOnUiThread {
                            tr = table.getChildAt(t.rowN) as TableRow
                            var tc : TextView = tr.getChildAt(t.columnN) as TextView
                            tc.text = "X"
                            tc.setOnClickListener {
                                Toast.makeText(context, "Asiento Ocupado", Toast.LENGTH_LONG).show()
                            }
                            }
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

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int, param3: Int) =
            EndCheckInFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                }
            }
    }
}