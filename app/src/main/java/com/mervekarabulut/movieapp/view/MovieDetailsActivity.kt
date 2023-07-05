package com.mervekarabulut.movieapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.mervekarabulut.movieapp.R
import com.mervekarabulut.movieapp.databinding.ActivityMovieDetailsBinding
import com.mervekarabulut.movieapp.model.Movie

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var firestore: FirebaseFirestore
    private var ratingInt : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        firestore = FirebaseFirestore.getInstance()

        val movie = intent.getSerializableExtra("movie") as? Movie
        if (movie != null) {
            binding.movieTitleTextView.text = movie.title
            binding.movieReleaseDateTextView.text = movie.releaseDate
            binding.movieOverviewTextView.text = movie.overview

            // Load the movie poster using Picasso or any other image loading library
            val posterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
            Glide.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.moviePosterImageView)
        }

        val rBar = findViewById<RatingBar>(R.id.ratingBar)
        rBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            // Retrieve the rating value
            ratingInt = rating.toInt()
            // You can use the selectedRating value as needed
        }

        binding.btnSubmit.setOnClickListener {
            saveCommentToFirestore()
        }

    }
    private fun saveCommentToFirestore() {

        val rating = ratingInt
        val comment = binding.commentEditText.text.toString()

        // Create a new comment document in Firestore
        val commentData = hashMapOf(
            "comment" to comment,
            "rating" to rating
        )

        firestore.collection("Comments")
            .add(commentData)
            .addOnSuccessListener {
                Toast.makeText(this, "Comment saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to save comment: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
