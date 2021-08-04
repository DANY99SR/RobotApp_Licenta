package com.example.robotapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.quicksettings.Tile
import android.system.Os.close
import android.system.Os.open
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.robotapp.Model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.nio.channels.DatagramChannel.open

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var firebaseUser: FirebaseUser
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // setSupportActionBar(toolbar);

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val userRef =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    nav_username_drawer.text = user!!.getUsername()
                    nav_email_drawer.text = user!!.getEmail()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true;

        toggle.syncState()
        drawerLayout.addDrawerListener(toggle)
        setToolbarTitle("Home")
        navView.setNavigationItemSelectedListener(this)
        changeFragment(HomeFragment())

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_home -> {
                setToolbarTitle("Home")
                changeFragment(HomeFragment())
            }
            R.id.nav_temperature -> {
                setToolbarTitle("Temperature")
                changeFragment(TemperatureFragment())
            }
            R.id.nav_humidity -> {
                setToolbarTitle("Humidity")
                changeFragment(HumidityFragment())
            }
            R.id.nav_air_quality -> {
                setToolbarTitle("Air Quality")
                changeFragment(AirQualityFragment())
            }
            R.id.nav_disinfection_quality -> {
                setToolbarTitle("Disinfection Quality")
                changeFragment(DisinfectionFragment())
            }
            R.id.nav_bluetooth -> {
                setToolbarTitle("Bluetooth")
                changeFragment(BluetoothFragment())
            }
            R.id.nav_log_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        return true
    }

    fun setToolbarTitle(tile: String) {
        supportActionBar?.title = tile
    }


    fun changeFragment(frag: Fragment) {
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_container, frag).commit()
    }
}