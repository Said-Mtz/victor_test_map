package com.example.examenfintecimal.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract val placeDao: PlaceDao

    companion object{
        const val DATABASE_NAME = "db_places"
    }
}