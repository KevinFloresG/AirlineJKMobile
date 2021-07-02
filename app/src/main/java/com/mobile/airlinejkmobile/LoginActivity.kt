package com.mobile.airlinejkmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.databinding.ActivityLoginBinding
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private var jUser: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get Sign In and Log In buttons
        val signInBtn = binding.registerBtn
        val logInBtn = binding.loginBtn

        // Setting Sign In Button On Click Listener
        signInBtn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    SignInActivity::class.java
                )
            )
        })

        // Setting Log In On Click Listener
        logInBtn.setOnClickListener(View.OnClickListener {
            val username = binding.lUsername.text.toString()
            val password = binding.lPassword.text.toString()

            // Stop and show alert if an input is empty
            if( username == "" || password == "" ) {
                Toast.makeText(
                    this,
                    "Indique el usuario y la clave para loguearse.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            // Verify user credentials with a request to the server
            jUser = verifyCredentials(username)

            // Deny access if the user does not exist or if the password is wrong
            if(jUser?.getString("username")  == "" || jUser == null
                || jUser?.getString("password") != password
                    || jUser?.getInt("isAdmin") != 0) {
                Toast.makeText(
                    this,
                    "El usuario no existe o la clave es incorrecta.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            val i = Intent(this, MainActivity::class.java)

            // Set logged user as an extra in the intent and start MainActivity
            i.putExtra("User",jUser.toString())
            startActivity(i)
        })

        val bundle = intent.extras
        val msg = bundle?.getString("msg")
        // Show message if this activity was started by another. For example if a user was registered without problems or
        // if the user logs out.
        if (msg != null) {
            Toast.makeText(this, "$msg", Toast.LENGTH_LONG).show()
        }

    }

    fun verifyCredentials(id: String): JSONObject? {
        var json: JSONObject? = null
        val thread = Thread {
            var apiUrl = "http://"+ Model.SERVER_IP+":8088/AirlineJK/users/get"
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