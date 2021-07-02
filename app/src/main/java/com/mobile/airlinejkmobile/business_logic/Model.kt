package com.mobile.airlinejkmobile.business_logic

import org.json.JSONArray
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

object Model {

    const val SERVER_IP = "10.0.2.2"

    var flights = HashMap<Int, Flight>()

    var reservationId = 0
    var reservations = ArrayList<Reservation>()

    var selectedFlight : Flight ? = null

    var currentUser:User? = null


    fun setCurrentUserFromJSON(uJ: JSONObject){

        currentUser = User(uJ.getString("username"),uJ.getString("name"),
            uJ.getString("lastname"),uJ.getString("email"),
            uJ.getString("dateOfBirth"),uJ.getString("address"),
            uJ.getString("workphone"), uJ.getString("cellphone")
        )
    }
// METODOS QUE OCUPA KEVIN PARA RESERVACIONES
    /*

     // Para insertar una reservacion - Devuelve el status code de la respuesta
 fun postReservation() : String{ // CAMBIAR DATOS QUEMADOS A REALES
        var responseCode = ""
        val thread = Thread {
            try {
                val url = URL("http://10.0.2.2:8088/AirlineJK/reservations/add")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.doOutput = true

                // Se arma el usuario
                val jsonUser = JSONObject()
                jsonUser.put("username","321") // Solo se ocupa el username

                // Se arma el tipo de pago (Que para móvil va a ser quemado a menos que se haga un select en algún lado)
                val jsonPType = JSONObject()
                jsonPType.put("code","CR")

                val jsonReservation = JSONObject()
                jsonReservation.put("user",jsonUser)
                jsonReservation.put("typeOfPayment",jsonPType)
                jsonReservation.put("seatQuantity", 1) // Aqui iria la cantidad de asientos
                jsonReservation.put("flightId",1) // Aqui el id del vuelo. OJO ! Esto no es referencia
                // a la tabla de vuelos, es solo un int para tener el valor a mano facilmente

                jsonReservation.put("flightInfo", "CRC-USA 14/05/2021 10:00 am") // Esto es solo un string para poner
                // un poco de información del vuelo sin ocupar tener una referencia a esa tabla

                jsonReservation.put("airplane", "BAD1315") // Esto es un string para poner
                // que el id del avión del vuelo, no es una referencia a la tabla, solo por facilidad

                jsonReservation.put("totalPrice", 15000.00) // Aqui va el precio total a pagar

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

	// Devuelve un JSONArray con las reservaciones hechas por el usuario, recibe el username como string
	fun getReservationsMadeByUser(username: String) : JSONArray?{
        var json: JSONArray? = null
        val thread = Thread {
            var apiUrl = "http://10.0.2.2:8088/AirlineJK/reservations/get/user"
            var current = ""

            val url: URL
            var urlConnection: HttpURLConnection? = null
            apiUrl += "?username=$username"
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

	// Actualiza la cantidad de asientos que ya se reservaron de una reserva. Devuelve el status code de la respuesta
	fun updateReservationCheckedInSeats(id: Int, newCheckedInQ: Int) : String{
        var responseCode = ""
        val thread = Thread {
            try {
                val url = URL("http://10.0.2.2:8088/AirlineJK/reservations/update")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "PUT"
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.doOutput = true

                // Se arma el usuario
                val json = JSONObject()
                json.put("id",id) // Id de la reservacion
                json.put("checkedInQuantity",newCheckedInQ) // Nueva cantidad de asientos ya reservados

                val os = DataOutputStream(conn.outputStream)
                os.writeBytes(json.toString())
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
        return  responseCode
    }




    // NUEVO - OBTENER RESERVACION ESPECIFICA POR ID

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

*/

    fun getReservationsMadeByUser(username: String) : JSONArray?{
        var json: JSONArray? = null
        val thread = Thread {
            var apiUrl = "http://$SERVER_IP:8088/AirlineJK/reservations/get/user"
            var current = ""

            val url: URL
            var urlConnection: HttpURLConnection? = null
            apiUrl += "?username=$username"
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

    fun setUserReservations() {
        reservations = ArrayList<Reservation>()

        var uReservations: JSONArray? = getReservationsMadeByUser(currentUser?.username!!)
        if(uReservations != null)
            if(uReservations?.length() != 0){
                for (i in 0 until uReservations?.length()!!) {
                    val item = uReservations.getJSONObject(i)
                    if(item.getInt("id") >= 1){
                        var schedule = Schedule("")
                        var route = Route("",0,0,schedule)
                        var flight = Flight(0,0.0,
                            0.0,1,"",route)
                        data class Schedule(var departureTime : String)
                        var res = Reservation(item.getInt("id"), flight,item.getInt("seatQuantity"),
                        item.getInt("checkedInQuantity"),item.getString("flightInfo"),
                            currentUser!!,item.getDouble("totalPrice"),"",Paymenttypes("CR"))
                        reservations.add(res)

                    }
                }
            }
    }

    fun getTicketsByReservation(id: Int) : JSONArray?{
        var json: JSONArray? = null
        val thread = Thread {
            var apiUrl = "http:/$SERVER_IP:8088/AirlineJK/tickets/get/reservation"
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

}