package com.challenge.nicedogs.data.api.repositories

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val totalBreeds: Int? = null
) {
    class Success<T>(data: T, totalBreeds: Int? = null) :
        Resource<T>(data = data, totalBreeds = totalBreeds)

    class Error<T>(val errorMessage: String) : Resource<T>(message = errorMessage)
    class Loading<T> : Resource<T>()
}