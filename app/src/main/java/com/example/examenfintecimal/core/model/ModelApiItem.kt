package com.example.examenfintecimal.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelApiItem(
    val id: Int = 1,
    val location: Location,
    val streetName: String,
    val suburb: String,
    val visited: Boolean
) : Parcelable {
}

fun Location.convertToString(): String {
    return "$latitude,$longitude"
}