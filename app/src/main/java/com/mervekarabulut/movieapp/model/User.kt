package com.mervekarabulut.movieapp.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val email: String? = null,
    val fullName : String? = null,
    ) {

}