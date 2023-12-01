package com.challenge.nicedogs.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.challenge.nicedogs.data.database.entities.BreedEntity

@Dao
interface BreedDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreeds(breeds: List<BreedEntity>)

    @Transaction
    @Query("SELECT * FROM BreedEntity WHERE page = :page ORDER BY name ASC")
    suspend fun getPagingBreeds(page: Int): List<BreedEntity>

    @Transaction
    @Query("SELECT * FROM BreedEntity WHERE name LIKE :query ORDER BY name ASC")
    suspend fun getBreedsBySearch(query: String): List<BreedEntity>

    @Transaction
    @Query("SELECT * FROM BreedEntity WHERE id = :id LIMIT 1")
    @Suppress("ROOM_WARNING")
    suspend fun getBreedById(id: Int): BreedEntity
}
