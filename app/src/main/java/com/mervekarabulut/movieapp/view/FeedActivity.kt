package com.mervekarabulut.movieapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mervekarabulut.movieapp.R
import com.mervekarabulut.movieapp.adapter.MovieAdapter
import com.mervekarabulut.movieapp.model.Movie
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import com.google.android.material.bottomnavigation.BottomNavigationView


class FeedActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieRecyclerView: RecyclerView
    private val allMovies: MutableList<Movie> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Home ekranına gitmek için gerekli işlemleri yapın
                    true
                }
                R.id.search -> {
                    // Search ekranına gitmek için gerekli işlemleri yapın
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

        // Initialize RecyclerView
        movieRecyclerView = findViewById(R.id.movieRecyclerView)
        movieRecyclerView.layoutManager = LinearLayoutManager(this)

        // Create an empty list of movies
        val movies = emptyList<Movie>()

        // Create an instance of the MovieAdapter and set it to the RecyclerView
        movieAdapter = MovieAdapter(movies)
        movieRecyclerView.adapter = movieAdapter

        // Fetch the movie data and update the adapter's data
        fetchMovies(1)
        fetchMovies(2)
    }

    private fun fetchMovies(page: Int) {
        val client = OkHttpClient()
        val url = "https://api.themoviedb.org/3/movie/popular?api_key=b7f287d6ba304e2225b5c9907410ff86&language=en-US&page=$page"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val fetchedMovies = parseMovieData(responseData)
                    runOnUiThread {
                        allMovies.addAll(fetchedMovies)
                        movieAdapter.setData(allMovies)
                    }
                } else {
                    // Handle API call failure
                }
            }

            // ...
        })
    }


    private fun parseMovieData(jsonData: String?): List<Movie> {
        val movies = mutableListOf<Movie>()
        try {
            val jsonObject = JSONObject(jsonData)
            val resultsArray = jsonObject.getJSONArray("results")
            for (i in 0 until resultsArray.length()) {
                val movieObject = resultsArray.getJSONObject(i)
                val id = movieObject.getInt("id")
                val title = movieObject.getString("title")
                val overview = movieObject.getString("overview")
                val releaseDate = movieObject.getString("release_date")
                val posterPath = movieObject.getString("poster_path")
                val movie = Movie(id, title, overview, releaseDate, posterPath)
                movies.add(movie)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return movies
    }

}