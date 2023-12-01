package com.challenge.nicedogs.domain.models

data class BreedDomainModel(
    val id: Int,
    val name: String?,
    val category: String?,
    val group: String?,
    val origin: String?,
    val temperament: String?,
    val image: String?
)
