package com.dastan.weatherfinal.data.firebase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Favorite(
    val id: String = "",
    val city: String = "",
    val note: String = "",
    val createdAt: Long = 0L,
    val createdBy: String = ""
) {
    constructor() : this("", "", "", 0L, "")
}
