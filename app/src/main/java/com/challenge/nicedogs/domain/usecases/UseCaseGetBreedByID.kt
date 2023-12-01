package com.challenge.nicedogs.domain.usecases

import com.challenge.nicedogs.domain.repository.BreedsDomainRepositoryContract

class UseCaseGetBreedByID(private val breedsDomainRepositoryContract: BreedsDomainRepositoryContract) {
    suspend operator fun invoke(id: Int) = breedsDomainRepositoryContract.getBreedByID(id)
}