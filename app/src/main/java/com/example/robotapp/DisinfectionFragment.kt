package com.example.robotapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.robotapp.Model.Data
import com.example.robotapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_disinfection.*
import kotlinx.android.synthetic.main.fragment_disinfection.view.*
import kotlinx.android.synthetic.main.fragment_home.*


class DisinfectionFragment : Fragment() {
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_disinfection, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val dataRef = FirebaseDatabase.getInstance().reference.child("Data").child(firebaseUser.uid)
        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.getValue<Data>(Data::class.java)
                    view.arc_progress_dis_temp.progress = data!!.getTemperature().toInt()
                    view.arc_progress_dis_hum.progress = data!!.getHumidity().toInt()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return view
    }

}