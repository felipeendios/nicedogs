package com.challenge.nicedogs.domain.usecases

import com.challenge.nicedogs.domain.repository.BreedsDomainRepositoryContract

class UseCaseGetBreedsByPage(private val breedsRepository: BreedsDomainRepositoryContract) {
    suspend operator fun invoke(page: Int) = breedsRepository.getBreedsByPage(page)
}