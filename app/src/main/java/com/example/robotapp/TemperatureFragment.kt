package com.example.robotapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.robotapp.Model.Data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.progress.progressview.ProgressView
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_temperature.*
import kotlinx.android.synthetic.main.fragment_temperature.view.*


class TemperatureFragment : Fragment() {

    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_temperature, container, false)
        val colorList = intArrayOf(Color.BLUE, Color.YELLOW, Color.RED)
        view.progressView_celsius.applyGradient(colorList)
        view.progressView_fahrenheit.applyGradient(colorList)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val x: Int = 25
        view.percent_temp_celsius.text = x.toString()
        view.progressView_celsius.progress = (x / 10) / 5.toFloat()

        view.percent_fahrenheit.text = ((x * 1.8).toInt() + 32).toString()
        view.progressView_fahrenheit.progress = (x / 10) / 5.toFloat()
        val dataRef = FirebaseDatabase.getInstance().reference.child("Data").child(firebaseUser.uid)
        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.getValue<Data>(Data::class.java)
                    view.progressView_celsius.progress =
                        (data!!.getTemperature().toFloat() / 10) / 5
                    view.percent_temp_celsius.text = data.getTemperature()

                    view.progressView_fahrenheit.progress =
                        (data!!.getTemperature().toFloat() / 10) / 5
                    view.percent_fahrenheit.text =
                        (((data.getTemperature().toInt() * 1.8).toInt()) + 32).toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        return view

    }

}