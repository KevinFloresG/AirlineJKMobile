package com.mobile.airlinejkmobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.business_logic.User


class UpdateUserInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_update_user_info, container, false)
        val user : User? = Model.currentUser

        // Show User Personal Information
        if(user != null) {
            val email = rootView.findViewById<View>(R.id.uuEmail) as TextView
            val address = rootView.findViewById<View>(R.id.uuAddress) as TextView
            val workphone = rootView.findViewById<View>(R.id.uuTelephone) as TextView
            val cellphone = rootView.findViewById<View>(R.id.uuCellphone) as TextView
            email.text = user.email
            address.text = user.address
            workphone.text = user.workphone
            cellphone.text = user.cellphone

            val btn = rootView.findViewById(R.id.saveUChangesBtn) as Button
            btn.setOnClickListener(View.OnClickListener {
                val newEmail = email.text.toString()
                val newAddress = address.text.toString()
                val newWorkphone = workphone.text.toString()
                val newCellphone = cellphone.text.toString()

                // Stop and show alert if an input is empty
                if( newEmail == "" || newAddress == "" || newWorkphone == "" || newCellphone =="") {
                    Toast.makeText(
                        context,
                        "Todos los campos deben estar llenos.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnClickListener
                }

                val frag = ProfileFragment()
                user.email = newEmail
                user.address = newAddress
                user.workphone = newWorkphone
                user.cellphone = newCellphone
                Model.updateUserInfo(user)
                Model.currentUser = user
                val fragment = activity?.supportFragmentManager?.beginTransaction()
                fragment?.replace(R.id.fragment_container, frag)?.commit()
            })


        }

        return rootView
    }




}