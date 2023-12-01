package com.challenge.nicedogs.domain.usecases

import com.challenge.nicedogs.domain.repository.BreedsDomainRepositoryContract

class UseCaseSearchBreeds(private val breedsRepository: BreedsDomainRepositoryContract) {
    suspend operator fun invoke(query: String) = breedsRepository.getBreedsBySearchQuery(query)
}