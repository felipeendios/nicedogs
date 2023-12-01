package com.challenge.nicedogs.domain.repository

import com.challenge.nicedogs.data.api.repositories.Resource
import com.challenge.nicedogs.domain.models.BreedDomainModel
import kotlinx.coroutines.flow.Flow

interface BreedsDomainRepositoryContract {
    suspend fun getBreedsByPage(page: Int): Flow<Resource<List<BreedDomainModel>>>
    suspend fun getBreedByID(id: Int): BreedDomainModel
    suspend fun getBreedsBySearchQuery(query: String): Flow<Resource<List<BreedDomainModel>>>
}
