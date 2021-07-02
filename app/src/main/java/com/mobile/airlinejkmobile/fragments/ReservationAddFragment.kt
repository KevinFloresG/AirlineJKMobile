package com.mobile.airlinejkmobile.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.beust.klaxon.Klaxon
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Flight
import com.mobile.airlinejkmobile.business_logic.FlightSeats
import com.mobile.airlinejkmobile.business_logic.Model
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URI
import java.net.URISyntaxException
import java.net.URL

private const val ID = "id"
private const val HOUR = "hour"
private const val ROUTE = "route"
private const val DATE = "date"
private const val DURATION = "duration"
private const val PRICE = "price"
private const val DISCOUNT = "discount"
private const val AVAILABLE_SEATS = "availableSeats"

class ReservationAddFragment : Fragment() {

    private var idFlight : Int? = null
    private var duration: String? = null
    private var hour: String? = null
    private var date: String? = null
    private var price: Double? = null
    private var discount: Double? = null
    private var availableSeats: Int? = null
    private var route: String? = null
    private var ws: WebSocketClient? = null
    private lateinit var seatsTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idFlight = it.getInt(ID)
            hour = it.getString(HOUR)
            route = it.getString(ROUTE)
            date = it.getString(DATE)
            duration = it.getString(DURATION)
            price = it.getDouble(PRICE)
            discount = it.getDouble(DISCOUNT)
            availableSeats = it.getInt(AVAILABLE_SEATS)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservation_add, container, false)
        view.findViewById<TextView>(R.id.route_txt).text = route
        view.findViewById<TextView>(R.id.discount_txt).text = discount.toString() + "%"
        view.findViewById<TextView>(R.id.duration_txt).text = duration
        view.findViewById<TextView>(R.id.hour_txt).text = hour
        view.findViewById<TextView>(R.id.date_txt).text = date
        view.findViewById<TextView>(R.id.price_txt).text = price.toString()
        seatsTextView = view.findViewById(R.id.seats_txt)
        seatsTextView.text = availableSeats.toString()
        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener {
            activity?.onBackPressed()
        }
        view.findViewById<Button>(R.id.add_btn).setOnClickListener {
            addReservation(view)
        }
        connectWebSocket()
        return view
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
                    "update" -> {
                        val f = Klaxon().parse<Flight>(content)!!
                        if(f.id == id){
                            activity?.runOnUiThread {
                                seatsTextView.text = f.availableSeats.toString()
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

    private fun addReservation(view: View){
        val cantSeats = view.findViewById<TextView>(R.id.cantSeats).text
        if (cantSeats.isBlank()){
            Toast.makeText(context, "Ingresa una Cant. de Asientos", Toast.LENGTH_SHORT).show()
            return
        }
        val cantInt = cantSeats.toString().toInt()
        if (cantInt > availableSeats!!) {
            Toast.makeText(context, "No hay tantos Asientos Disponibles!", Toast.LENGTH_SHORT).show()
            return
        }
        if (cantInt <= 0){
            Toast.makeText(context, "Ingresa una Cant. de Asientos Correcta", Toast.LENGTH_SHORT).show()
            return
        }
        ws?.send("{\"type\":\"updateSeats\",\"id\":$idFlight,\"availableSeats\":${availableSeats!! - cantInt}}")
        if(postReservation(cantInt) == HttpURLConnection.HTTP_OK.toString()){
            Toast.makeText(context, "Reservación realizada Correctamente", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, "No se pudo realizar la Reservación", Toast.LENGTH_LONG).show()
        }
        activity?.onBackPressed()
    }

    private fun postReservation(cantS : Int) : String{ // CAMBIAR DATOS QUEMADOS A REALES
        var responseCode = ""
        val thread = Thread {
            try {
                val url = URL("http://${Model.SERVER_IP}:8088/AirlineJK/reservations/add")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.doOutput = true

                // Se arma el usuario
                val jsonUser = JSONObject()
                jsonUser.put("username", Model.currentUser?.username) // Solo se ocupa el username

                // Se arma el tipo de pago (Que para móvil va a ser quemado a menos que se haga un select en algún lado)
                val jsonPType = JSONObject()
                jsonPType.put("code","CR")

                val jsonReservation = JSONObject()
                jsonReservation.put("user",jsonUser)
                jsonReservation.put("typeOfPayment",jsonPType)
                jsonReservation.put("seatQuantity", cantS) // Aqui iria la cantidad de asientos
                jsonReservation.put("flightId",idFlight) // Aqui el id del vuelo. OJO ! Esto no es referencia
                // a la tabla de vuelos, es solo un int para tener el valor a mano facilmente

                jsonReservation.put("flightInfo", "$route $date $hour") // Esto es solo un string para poner
                // un poco de información del vuelo sin ocupar tener una referencia a esa tabla

                jsonReservation.put("airplane", "BAD1315") // Esto es un string para poner
                // que el id del avión del vuelo, no es una referencia a la tabla, solo por facilidad

                jsonReservation.put("totalPrice", cantS * price!!) // Aqui va el precio total a pagar

                val os = DataOutputStream(conn.outputStream)
                os.writeBytes(jsonReservation.toString()) // Aqui se pasa la vara como un string JSON
                os.flush()
                os.close()
                responseCode = conn.responseCode.toString()
                conn.disconnect()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
        return responseCode
    }

    companion object {
        @JvmStatic
        fun newInstance(flight: Flight) =
            ReservationAddFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID, flight.id)
                    putString(HOUR, flight.route.schedule.departureTime)
                    putString(ROUTE, flight.route.id)
                    putString(DATE, flight.departureDate.split("T")[0])
                    putString(DURATION, "${flight.route.durationhours}:${flight.route.durationminutes}")
                    putDouble(PRICE, flight.price)
                    putDouble(DISCOUNT, flight.discount * 100)
                    putInt(AVAILABLE_SEATS, flight.availableSeats)
                }
            }
    }
}