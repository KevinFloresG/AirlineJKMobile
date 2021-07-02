package com.mobile.airlinejkmobile.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
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
        //connectWebSocket()
        return view
    }

    private fun printSeats(view : View, tickets : JSONArray?){
        data class Ticket(var rowN : Int, var columnN : Int)
        val list = Klaxon().parseArray<Ticket>(tickets.toString())
        val table = view.findViewById<TableLayout>(R.id.tlayt)
        var tr : TableRow
        var tc : TextView
        for(t in list!!){
            tr = table.getChildAt(t.rowN) as TableRow
            tc = tr.getChildAt(t.columnN) as TextView
            tc.text = "ocupado"
        }
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

    private fun connectWebSocket() {
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
                    "update" -> {

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