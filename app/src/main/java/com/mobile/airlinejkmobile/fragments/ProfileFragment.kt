package com.mobile.airlinejkmobile.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mobile.airlinejkmobile.R
import com.mobile.airlinejkmobile.business_logic.Model
import com.mobile.airlinejkmobile.business_logic.User


class ProfileFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val user : User? = Model.currentUser

        // Show User Personal Information
        if(user != null) {
            val username = rootView.findViewById<View>(R.id.pUsername) as TextView
            val name = rootView.findViewById<View>(R.id.pName) as TextView
            val lastName = rootView.findViewById<View>(R.id.pLastname1) as TextView
            val lastName2 = rootView.findViewById<View>(R.id.pLastname2) as TextView
            val dateOfBirth = rootView.findViewById<View>(R.id.pDateOfBirth) as TextView
            val email = rootView.findViewById<View>(R.id.pEmail) as TextView
            val address = rootView.findViewById<View>(R.id.pLocation) as TextView
            val workphone = rootView.findViewById<View>(R.id.pTelephone) as TextView
            val cellphone = rootView.findViewById<View>(R.id.pCellphone) as TextView

            username.text = user.username
            name.text = user.name
            lastName.text = user.lastName
            lastName2.text = user.lastName2
            dateOfBirth.text = user.dateOfBirth
            email.text = user.email
            address.text = user.address
            workphone.text = user.workphone
            cellphone.text = user.cellphone

            val btn = rootView.findViewById(R.id.enUUpdBtn) as Button
            btn.setOnClickListener(View.OnClickListener {
                val frag = UpdateUserInfoFragment()
                val fragment = activity?.supportFragmentManager?.beginTransaction()
                fragment?.replace(R.id.fragment_container, frag)?.addToBackStack(null)?.commit()
            })

        }

        return rootView
    }




}