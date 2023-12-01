package com.challenge.nicedogs.data.api.models

import com.google.gson.annotations.SerializedName

data class BreedModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("breed_group") val group: String?,
    @SerializedName("bred_for") val category: String?,
    @SerializedName("image") val image: BreedModelImage?,
    @SerializedName("temperament") val temperament: String?,
    @SerializedName("origin") val origin: String?

)

data class BreedModelImage(
    @SerializedName("url") val url: String?
)
