package com.mobile.airlinejkmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mobile.airlinejkmobile.databinding.ActivitySignIn2Binding


class SignInActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivitySignIn2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignIn2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        val name = bundle!!.getString("Name")
        val lastName = bundle!!.getString("LastName1")
        val lastName2 = bundle!!.getString("LastName2")
        val email = bundle!!.getString("Email")
        val dateOfBirth = bundle!!.getString("DateOfBirth")

        Toast.makeText(this, "$name-$lastName-$dateOfBirth", Toast.LENGTH_LONG).show()

        val btn = binding.signInBtn


    }
}