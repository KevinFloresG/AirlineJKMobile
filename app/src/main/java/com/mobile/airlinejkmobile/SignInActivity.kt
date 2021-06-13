package com.mobile.airlinejkmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mobile.airlinejkmobile.databinding.ActivityLoginBinding
import com.mobile.airlinejkmobile.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btn = binding.ctnBtn

        btn.setOnClickListener(View.OnClickListener {

            val i = Intent(this, SignInActivity2::class.java)
            val name = binding.iName.text.toString()
            val lastName1 = binding.iLastName1.text.toString()
            val lastName2 = binding.iLastName2.text.toString()
            val email = binding.iEmail.text.toString()
            val dateOfB = ""+ binding.iDatePicker.year +"-"+ (binding.iDatePicker.month + 1) +"-"+ binding.iDatePicker.dayOfMonth

            if(name == ""|| lastName1 == "" || lastName2 == "" || email == "" || dateOfB == "")
                Toast.makeText(this, "Debe llenar todos los campos adecuadamente para seguir.", Toast.LENGTH_LONG).show()
            else {
                i.putExtra("Name", name)
                i.putExtra("LastName1", lastName1)
                i.putExtra("LastName2", lastName2)
                i.putExtra("Email", email)
                i.putExtra("DateOfBirth", dateOfB)
                startActivity(i)
            }
        })


    }
}