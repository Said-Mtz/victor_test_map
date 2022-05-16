package com.example.examenfintecimal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.examenfintecimal.core.model.Location
import com.example.examenfintecimal.core.model.ModelApiItem

@Entity(tableName = "places")//Tabla
data class PlaceEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "streetName")
    val streetName: String,

    @ColumnInfo(name = "suburb")
    val suburb: String,

    @ColumnInfo(name = "visited")
    val visited: Boolean
)