package com.example.examenfintecimal.core.api

import com.example.examenfintecimal.core.model.ModelApiList
import retrofit2.Call
import retrofit2.http.GET

interface ApiPlaceService {

    @GET("interview")
    fun obtenerPlace(): Call<ModelApiList>

}