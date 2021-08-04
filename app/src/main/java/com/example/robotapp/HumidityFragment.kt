package com.example.robotapp

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.robotapp.Model.Data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_disinfection.view.*
import kotlinx.android.synthetic.main.fragment_humidity.*
import kotlinx.android.synthetic.main.fragment_humidity.view.*


class HumidityFragment : Fragment() {

    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_humidity, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val dataRef = FirebaseDatabase.getInstance().reference.child("Data").child(firebaseUser.uid)
        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.getValue<Data>(Data::class.java)
                    view.arc_progress_humidity.progress = data!!.getHumidity().toInt()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        return view
    }

}