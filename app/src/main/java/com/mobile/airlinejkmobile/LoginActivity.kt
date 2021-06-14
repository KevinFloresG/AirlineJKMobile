package com.mobile.airlinejkmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.databinding.ActivityLoginBinding
import com.mobile.airlinejkmobile.databinding.NavigationDrawerHeaderBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

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


            val i = Intent(this, MainActivity::class.java)
            val userToLogIn = Model.login(username,password)

            // Deny access if the user does not exist or if the password is wrong
            if(userToLogIn == null){
                Toast.makeText(
                    this,
                    "El usuario no existe o la clave es incorrecta.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            // Set logged user as an extra in the intent and start MainActivity
            i.putExtra("User",userToLogIn)
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
}