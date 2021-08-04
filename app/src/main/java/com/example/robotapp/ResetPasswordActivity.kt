package com.example.robotapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_passoword.*

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_passoword)

        button_reset.setOnClickListener {
            ResetPassword()
        }
    }

    private fun ResetPassword() {
        val email = email_reset.text.toString()

        when {
            TextUtils.isEmpty(email) -> Toast.makeText(this, "email is required", Toast.LENGTH_LONG)
                .show()

            else -> {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Success reset", Toast.LENGTH_LONG).show()
                    } else {
                        val message = task.exception!!.toString()
                        Toast.makeText(this, "Error $message", Toast.LENGTH_LONG).show()

                    }

                }
            }
        }
    }
}