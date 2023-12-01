package com.challenge.nicedogs.domain

import com.challenge.nicedogs.data.api.models.BreedModel
import com.challenge.nicedogs.data.database.entities.BreedEntity
import com.challenge.nicedogs.domain.models.BreedDomainModel

fun List<BreedEntity>.asListBreedDomainModel() = map { breed ->
    breed.asBreedDomainModel()
}

fun BreedEntity.asBreedDomainModel() = BreedDomainModel(
    id = id,
    name = name,
    category = category,
    origin = origin,
    group = group,
    image = image,
    temperament = temperament
)

fun List<BreedModel>.asBreedDTOList(page: Int) = map { breed ->
    breed.asBreedEntity(page).also { if (page > 0) it.page = page }
}

fun BreedModel.asBreedEntity(page : Int) = BreedEntity(
    id = id,
    name = name,
    category = category,
    origin = origin,
    group = group,
    image = image?.url,
    temperament = temperament,
    page = page
)
