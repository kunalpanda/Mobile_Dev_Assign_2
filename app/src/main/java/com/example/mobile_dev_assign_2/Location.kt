package com.example.mobile_dev_assign_2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
