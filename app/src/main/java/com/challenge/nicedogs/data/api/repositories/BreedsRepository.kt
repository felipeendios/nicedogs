package com.challenge.nicedogs.data.api.repositories

import com.challenge.nicedogs.data.api.models.BreedModel
import com.challenge.nicedogs.data.api.retrofit.APIEndpoints

class BreedsRepository(private val apiEndpoints: APIEndpoints) {

    suspend fun getBreeds(pageValue: Int): Resource<List<BreedModel>> =
        safeCallToAPI { apiEndpoints.getBreeds(pageValue, ITEMS_PER_PAGE) }

    suspend fun searchBreeds(query: String): Resource<List<BreedModel>> =
        safeCallToAPI { apiEndpoints.searchBreeds(query) }

    companion object {
        private const val ITEMS_PER_PAGE = 10
    }
}
