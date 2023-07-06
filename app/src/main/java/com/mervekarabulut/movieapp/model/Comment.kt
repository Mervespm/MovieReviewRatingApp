package com.mervekarabulut.movieapp.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comment(
    val movieName: String? = null,
    val rating : Int? = null,
    val userEmail: String? = null,
    val comment:  String? = null,
) {

}