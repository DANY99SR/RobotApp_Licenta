package com.example.robotapp

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
import kotlinx.android.synthetic.main.fragment_air_quality.*
import kotlinx.android.synthetic.main.fragment_home.*


class AirQualityFragment : Fragment() {

    private lateinit var firebaseUser: FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_air_quality, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val dataRef = FirebaseDatabase.getInstance().reference.child("Data").child(firebaseUser.uid)
        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.getValue<Data>(Data::class.java)
                    if (data!!.getAirQuality() != null) {
                        arc_progress.progress = data!!.getAirQuality().toInt()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return view
    }


}