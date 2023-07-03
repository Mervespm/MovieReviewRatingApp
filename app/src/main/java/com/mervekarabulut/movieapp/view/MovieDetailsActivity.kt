package com.mervekarabulut.movieapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.mervekarabulut.movieapp.R
import com.mervekarabulut.movieapp.databinding.ActivityMovieDetailsBinding
import com.mervekarabulut.movieapp.model.Movie

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
    }
}
