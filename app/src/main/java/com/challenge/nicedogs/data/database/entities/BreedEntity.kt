package com.challenge.nicedogs.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BreedEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo("name") val name: String?,
    @ColumnInfo("bred_for") val category: String?,
    @ColumnInfo("breed_group") val group: String?,
    @ColumnInfo("origin") val origin: String?,
    @ColumnInfo("temperament") val temperament: String?,
    @ColumnInfo("image_url") val image: String?,
    @ColumnInfo("page") var page: Int
)
