package com.example.examenfintecimal.utils

import android.content.Context
import android.text.InputFilter
import android.widget.EditText
import androidx.core.content.ContextCompat

fun setColor(context: Context, color: Int) = ContextCompat.getColor(context, color)

fun EditText.filterEmoji() {
    filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
        source.filter { Character.getType(it) != Character.SURROGATE.toInt() }
    })
}