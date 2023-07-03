package com.mervekarabulut.movieapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mervekarabulut.movieapp.R
import com.mervekarabulut.movieapp.databinding.ActivityLoginBinding
import com.mervekarabulut.movieapp.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intent = Intent(applicationContext, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnLogin = findViewById<Button>(binding.btnLogin.id)
        btnLogin.setOnClickListener {
            signIn()
        }


    }

    fun signIn() {

        val userEmail = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        if (userEmail.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(userEmail,password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(applicationContext,"Welcome: ${auth.currentUser?.email.toString()}",Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }

            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }



    }
}
