package com.mervekarabulut.movieapp.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mervekarabulut.movieapp.R
import com.mervekarabulut.movieapp.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var binding : ActivityUserProfileBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.imageView.setImageURI(selectedImageUri)
            uploadProfileImage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val intent = Intent(this, FeedActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.search -> {
                    val intent = Intent(this, CommentList::class.java)
                    startActivity(intent)
                    true
                }
                R.id.person -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        loadProfileDetails()

        binding.imageView.setOnClickListener {
            openGallery()
        }

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    private fun loadProfileDetails() {
        val userId = auth.currentUser?.uid
        val email = auth.currentUser?.email

        if (userId != null) {
            val userDocument = firestore.collection("Users").document(userId)
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val fullName = documentSnapshot.getString("fullName")
                        val profileImageUrl = documentSnapshot.getString("profileImageUrl")
                        binding.textView.text = fullName
                        binding.email.text = email
                        // Load and display the profile image using an image loading library
                        Glide.with(this)
                            .load(profileImageUrl)
                            .into(binding.imageView)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure case
                }


        }




    }

    private fun openGallery() {
        getContent.launch("image/*")
    }


    private fun uploadProfileImage() {
        val userId = auth.currentUser?.uid
        userId?.let { uid ->
            selectedImageUri?.let { imageUri ->
                val profileImageRef = storageReference.child("profile_images/$uid")
                profileImageRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        // Image upload success
                        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                            val profileImageUrl = uri.toString()
                            // Save the profileImageUrl to the user's document in Firestore
                            val userDocumentRef = firestore.collection("Users").document(uid)
                            userDocumentRef.update("profileImageUrl", profileImageUrl)
                                .addOnSuccessListener {
                                    // Profile image URL saved successfully
                                }
                                .addOnFailureListener { exception ->
                                    // Handle the failure case
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle image upload failure
                    }
            }
        }
    }




}