package com.example.examenfintecimal.core.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    fun instance(): Retrofit = Retrofit.Builder()
        .baseUrl("https://fintecimal-test.herokuapp.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}