package com.challenge.nicedogs.data.api.retrofit

import com.challenge.nicedogs.data.api.models.BreedModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIEndpoints {

    @GET("breeds")
    suspend fun getBreeds(
        @Query("page") pageNumber: Int,
        @Query("limit") pageLimit: Int
    ): Response<List<BreedModel>>

    @GET("breeds/search")
    suspend fun searchBreeds(
        @Query("q") query: String
    ): Response<List<BreedModel>>
}
