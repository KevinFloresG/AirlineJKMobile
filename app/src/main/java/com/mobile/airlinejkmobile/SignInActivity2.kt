package com.mobile.airlinejkmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mobile.airlinejkmobile.databinding.ActivitySignIn2Binding
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class SignInActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivitySignIn2Binding
    private var jUser: JSONObject? = null
    private lateinit var name : String
    private lateinit var lastName : String
    private lateinit var lastName2 : String
    private lateinit var email : String
    private lateinit var dateOfBirth : String
    private lateinit var location : String
    private lateinit var username : String
    private lateinit var telephone : String
    private lateinit var cellphone : String
    private lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignIn2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        name = bundle?.getString("Name")!!
        lastName = bundle?.getString("LastName1")!!
        lastName2 = bundle?.getString("LastName2")!!
        email = bundle?.getString("Email")!!
        dateOfBirth = bundle?.getString("DateOfBirth")!!

        val btn = binding.signInBtn

        btn.setOnClickListener(View.OnClickListener {

            location = binding.iLocation.text.toString()
            username = binding.iUsername.text.toString()
            telephone = binding.iPhone.text.toString()
            cellphone = binding.iCellphone.text.toString()
            password = binding.iPassword.text.toString()
            val cPassword = binding.iCPassword.text.toString()

            if(location == ""|| username == "" || telephone == ""
                || cellphone == "" || password == "" || cPassword == "") {
                Toast.makeText(
                    this,
                    "Debe llenar todos los campos completar el registro.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            jUser  = getUserByUsername(username)

            if(jUser?.getString("username") != ""){
                Toast.makeText(
                    this,
                    "El username seleccionado ya existe. Indique otro.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            if(password != cPassword){
                Toast.makeText(
                    this,
                    "Advertencia: Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            var addStatusCode = postClientUser()

            if(addStatusCode != "200"){
                Toast.makeText(
                    this,
                    "Error del servidor, no se pudo agregar el usuario indicado.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            val i = Intent(this, LoginActivity::class.java)
            i.putExtra("msg", "Se ha registrado con éxito el usuario.")
            startActivity(i)
        })

    }

    fun postClientUser() : String {
        var responseCode = ""
        val thread = Thread {
            try {
                val url = URL("http://10.0.2.2:8088/AirlineJK/users/add")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.doOutput = true
                conn.doInput = true
                val jsonParam = JSONObject()
                jsonParam.put("username", username)
                jsonParam.put("password", password)
                jsonParam.put("name", name)
                jsonParam.put("lastname", "$lastName $lastName2")
                jsonParam.put("email", email)
                jsonParam.put("address", location)
                jsonParam.put("workphone", telephone)
                jsonParam.put("cellphone", cellphone)
                jsonParam.put("dateOfBirth", dateOfBirth+"T00:00:00.000-0600")
                jsonParam.put("isAdmin", 0)
                val os = DataOutputStream(conn.outputStream)
                os.writeBytes(jsonParam.toString())
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

    fun getUserByUsername(id: String): JSONObject? {
        var json: JSONObject? = null
        val thread = Thread {
            var apiUrl = "http://192.168.0.2:8088/AirlineJK/users/get"
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