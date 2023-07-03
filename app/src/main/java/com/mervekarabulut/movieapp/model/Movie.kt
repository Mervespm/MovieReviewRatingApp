package com.mervekarabulut.movieapp.model

import java.io.Serializable

data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val overview: String,
    val posterPath: String
) : Serializable
