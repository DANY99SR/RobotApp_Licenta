package com.example.robotapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        log_in_already.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        button_register.setOnClickListener {
            CreateAccount()
        }
    }

    private fun CreateAccount() {

        val fullName = fullname_register.text.toString()
        val username = username_register.text.toString()
        val email = email_register.text.toString()
        val password = password_register.text.toString()
        val robotpassword = robot_password.text.toString()

        if(robotpassword == "1999")
        {
        when {
            TextUtils.isEmpty(fullName) -> Toast.makeText(
                this,
                "full name is required",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(username) -> Toast.makeText(
                this,
                "username is required",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this, "email is required", Toast.LENGTH_LONG)
                .show()
            TextUtils.isEmpty(password) -> Toast.makeText(
                this,
                "password is required",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                val progressDialog = ProgressDialog(this@RegisterActivity)
                progressDialog.setTitle("Sign Up")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveUserInfo(fullName, username, email, password, progressDialog)

                        } else {
                            val message = task.exception!!.toString()
                            Toast.makeText(this, "Error $message", Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }
    }
        else {
            Toast.makeText(this,"Parola incorecta robot",Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserInfo(
        fullName: String,
        username: String,
        email: String,
        password: String,
        progressDialog: ProgressDialog
    ) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = fullName
        userMap["username"] = username
        userMap["email"] = email
        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Account has been created successfully", Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Error $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
}