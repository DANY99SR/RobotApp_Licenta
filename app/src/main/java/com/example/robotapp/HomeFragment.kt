package com.example.robotapp

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.robotapp.Model.Data
import com.example.robotapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_temperature.view.*


class HomeFragment : Fragment() {

    private lateinit var firebaseUser: FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val userRef =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    hello_username.text = "Hello " + user!!.getUsername()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        val dataRef = FirebaseDatabase.getInstance().reference.child("Data").child(firebaseUser.uid)
        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.getValue<Data>(Data::class.java)
                    view.progressView_battery.progress = (data!!.getBattery().toFloat() / 100)
                    view.percent_battery.text = data.getBattery()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val colorList = intArrayOf(Color.RED, Color.YELLOW, Color.GREEN)
        view.progressView_battery.applyGradient(colorList)
        return view
    }

}