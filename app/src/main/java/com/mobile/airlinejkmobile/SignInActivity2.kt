package com.mobile.airlinejkmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.business_logic.User
import com.mobile.airlinejkmobile.databinding.ActivitySignIn2Binding


class SignInActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivitySignIn2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignIn2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        val name = bundle?.getString("Name")
        val lastName = bundle?.getString("LastName1")
        val lastName2 = bundle?.getString("LastName2")
        val email = bundle?.getString("Email")
        val dateOfBirth = bundle?.getString("DateOfBirth")

        val btn = binding.signInBtn

        btn.setOnClickListener(View.OnClickListener {


            val location = binding.iLocation.text.toString()
            val username = binding.iUsername.text.toString()
            val telephone = binding.iPhone.text.toString()
            val cellphone = binding.iCellphone.text.toString()
            val password = binding.iPassword.text.toString()
            val cPassword = binding.iCPassword.text.toString()

            if(location == ""|| username == "" || telephone == "" || cellphone == "" || password == "" || cPassword == "") {
                Toast.makeText(
                    this,
                    "Debe llenar todos los campos completar el registro.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            val userExists = Model.getUserByUsername(username)

            if(userExists != null){
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

            val i = Intent(this, SignInActivity2::class.java)
            i.putExtra("msg", "Se ha registrado con exito.")
            val userToRegister = User(username,password,name!!,
                lastName!!,lastName2!!,email!!,dateOfBirth!!,location,telephone,cellphone,0)
            Model.addUser(userToRegister)
            val userToTest = Model.getUserByUsername(username)
            var txt = "Hell no"
            if(userToTest != null)
                txt = "Yeah boy"
            Log.d("Hmmm",txt)
            startActivity(i)

        })


    }
}