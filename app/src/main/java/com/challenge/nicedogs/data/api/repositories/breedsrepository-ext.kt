package com.challenge.nicedogs.data.api.repositories

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeCallToAPI(call: suspend () -> Response<T>): Resource<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val totalBreeds =
                response.headers().firstOrNull { it.first == "pagination-count" }?.second
            Resource.Success(
                data = response.body()
                    ?: throw NullPointerException("NullPointerException - response.body()"),
                totalBreeds?.toInt()
            )
        } else {
            val errorBody = response.errorBody()?.string()
            Resource.Error(errorMessage = errorBody ?: "response is not Successful")
        }
    } catch (e: Exception) {
        Resource.Error(errorMessage = "Exception ${e.message}")
    } catch (e: IOException) {
        Resource.Error(errorMessage = "Check your internet connection ${e.message}")
    } catch (e: HttpException) {
        Resource.Error(errorMessage = e.message ?: "HttpException")
    }
}