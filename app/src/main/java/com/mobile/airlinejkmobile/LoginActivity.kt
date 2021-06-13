package com.mobile.airlinejkmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signInBtn = binding.registerBtn
        val logInBtn = binding.loginBtn

        signInBtn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    SignInActivity::class.java
                )
            )
        })

        logInBtn.setOnClickListener(View.OnClickListener {
            val username = binding.lUsername.text.toString()
            val password = binding.lPassword.text.toString()

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

            if(userToLogIn == null){
                Toast.makeText(
                    this,
                    "El usuario no existe o la clave es incorrecta.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            i.putExtra("User",userToLogIn)
            startActivity(i)
        })

        val bundle = intent.extras
        val msg = bundle?.getString("msg")
        if (msg != null) {
            Toast.makeText(this, "$msg", Toast.LENGTH_LONG).show()
        }

    }
}