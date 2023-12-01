package com.challenge.nicedogs.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.challenge.nicedogs.data.database.dao.BreedDAO
import com.challenge.nicedogs.data.database.entities.BreedEntity

@Database(entities = [BreedEntity::class], version = 1, exportSchema = false)
abstract class NiceDogsDatabase : RoomDatabase() {

    abstract fun breedDAO(): BreedDAO

    companion object {
        @Volatile
        private var INSTANCE: NiceDogsDatabase? = null

        fun getInstance(context: Context): NiceDogsDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): NiceDogsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                NiceDogsDatabase::class.java,
                "nice_dogs_database"
            ).build()
        }
    }
}
