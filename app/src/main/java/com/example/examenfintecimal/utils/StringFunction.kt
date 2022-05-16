package com.example.examenfintecimal.utils

import android.util.Log

fun String.log(tag: String, error: Boolean = true) {
    if (error) Log.e(tag, this) else Log.d(tag, this)
}