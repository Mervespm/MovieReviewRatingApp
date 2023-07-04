package com.mervekarabulut.movieapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

import com.mervekarabulut.movieapp.R

import com.mervekarabulut.movieapp.databinding.ActivityRegisterBinding
import com.mervekarabulut.movieapp.model.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val userEmail = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val fullName = binding.etFullName.text.toString()

        if (userEmail.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val currentUser = User(userEmail, fullName)

                    val userCollection = firestore.collection("Users")
                    userCollection.document(auth.currentUser!!.uid)
                        .set(currentUser, SetOptions.merge())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, FeedActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { err ->
                            Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                        }

                }

            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()

            }
        }
    }
}




