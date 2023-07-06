package com.mervekarabulut.movieapp.view

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mervekarabulut.movieapp.R
import com.mervekarabulut.movieapp.databinding.ActivityMovieDetailsBinding
import com.mervekarabulut.movieapp.model.Movie

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var ratingInt : Int = 0
    var movieName: String ?= null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        val movie = intent.getSerializableExtra("movie") as? Movie
        if (movie != null) {
            binding.movieTitleTextView.text = movie.title
            movieName = movie.title
            binding.movieReleaseDateTextView.text = movie.releaseDate
            binding.movieOverviewTextView.text = movie.overview

            // Load the movie poster using Picasso or any other image loading library
            val posterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
            Glide.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.moviePosterImageView)
        }

        binding.btnSubmit.setOnClickListener {
            saveCommentToFirestore()
        }



    }
    private fun saveCommentToFirestore() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_layout)
        dialog.show()
        val ratingBar = dialog.findViewById<RatingBar>(R.id.ratingBar)
        val commentEditText = dialog.findViewById<EditText>(R.id.commentEditText)
        val shareButton = dialog.findViewById<Button>(R.id.shareButton)

        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingInt = rating.toInt()
        }

        shareButton.setOnClickListener {
            val comment = commentEditText.text.toString()
            val rating = ratingInt
            val userEmail = auth.currentUser?.email

            // Create a new comment document in Firestore
            val commentData = hashMapOf(
                "comment" to comment,
                "rating" to rating,
                "userEmail" to userEmail,
                "movieName" to movieName
            )

            firestore.collection("Comments")
                .add(commentData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Comment saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to save comment: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            dialog.dismiss()
        }



    }
}
