package com.mervekarabulut.movieapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mervekarabulut.movieapp.R
import com.mervekarabulut.movieapp.model.Movie
import com.mervekarabulut.movieapp.view.MovieDetailsActivity

class MovieAdapter(private var movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private val movieList: MutableList<Movie> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item_layout, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(newMovies: List<Movie>) {
        movieList.clear()
        movies = newMovies
        movieList.addAll(movies)
        notifyDataSetChanged()
    }
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val moviePosterImageView: ImageView = itemView.findViewById(R.id.moviePosterImageView)
        private val movieTitleTextView: TextView = itemView.findViewById(R.id.movieTitleTextView)
        private val movieOverviewTextView: TextView = itemView.findViewById(R.id.movieOverviewTextView)

        fun bind(movie: Movie) {
            movieTitleTextView.text = movie.title
            movieOverviewTextView.text = movie.overview

            val posterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
            Glide.with(itemView)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(moviePosterImageView)
        }
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = movieList[position]
                    val intent = Intent(itemView.context, MovieDetailsActivity::class.java)
                    intent.putExtra("movie", movie)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }
}

