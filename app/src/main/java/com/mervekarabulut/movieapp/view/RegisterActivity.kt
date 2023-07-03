package com.mervekarabulut.movieapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.mervekarabulut.movieapp.R

import com.mervekarabulut.movieapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val userEmail = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (userEmail.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val intent = Intent(applicationContext, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()

            }
        }
    }
}




