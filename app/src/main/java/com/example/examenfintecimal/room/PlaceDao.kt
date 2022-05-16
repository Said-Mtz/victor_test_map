package com.example.examenfintecimal.room

import androidx.room.*
import com.example.examenfintecimal.core.model.Location
import com.example.examenfintecimal.core.model.ModelApiItem
import com.example.examenfintecimal.core.model.ModelApiList

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: PlaceEntity)

    @Update
    fun update(place: PlaceEntity)

    @Query("SELECT * FROM places")//Lista completa de los lugares
    fun getAllPlace(): List<PlaceEntity>
}

fun List<PlaceEntity>.placesEntityToModelApiList(): ModelApiList? {
    val list = ModelApiList()
    forEachIndexed { index, placeEntity ->
        list.add(
            ModelApiItem(
                id = placeEntity.id,
                location = Location(
                    latitude = placeEntity.location.substringBefore(",").toDouble(),
                    longitude = placeEntity.location.substringAfter(",").toDouble()
                ),
                streetName = placeEntity.streetName,
                suburb = placeEntity.suburb,
                visited = placeEntity.visited
            )
        )
    }
    return list
}