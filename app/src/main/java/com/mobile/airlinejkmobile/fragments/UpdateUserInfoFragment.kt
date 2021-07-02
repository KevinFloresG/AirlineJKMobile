package com.mobile.airlinejkmobile.fragments

import android.os.Bundle
import android.util.Log
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
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL


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
                var updResponse = updateUserInfo(user.username,newEmail,newAddress,newWorkphone,newCellphone)

                if(updResponse != "200") {
                    Toast.makeText(
                        context,
                        "Debido a un error del servidor no se pudo actualizar la informacion.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnClickListener
                }

                Model.currentUser = user
                val fragment = activity?.supportFragmentManager?.beginTransaction()
                fragment?.replace(R.id.fragment_container, frag)?.commit()
            })


        }

        return rootView
    }

    fun updateUserInfo(username: String, email: String, address: String, workphone: String, cellphone: String) : String{
        var responseCode = ""
        val thread = Thread {
            try {
                val url = URL("http://"+Model.SERVER_IP+":8088/AirlineJK/users/update/info")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "PUT"
                conn.setRequestProperty("Content-Type", "application/json")
                // conn.setRequestProperty("Accept", "application/json")
                conn.doOutput = true
                val jsonParam = JSONObject()
                jsonParam.put("username", username)
                jsonParam.put("email", email)
                jsonParam.put("address", address)
                jsonParam.put("workphone", workphone)
                jsonParam.put("cellphone", cellphone)
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




}