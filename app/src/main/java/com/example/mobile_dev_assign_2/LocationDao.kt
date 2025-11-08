package com.example.mobile_dev_assign_2

import androidx.room.*

@Dao
interface LocationDao {
    // Query by address (full or partial)
    @Query("SELECT * FROM locations WHERE address LIKE '%' || :address || '%' LIMIT 1")
    fun searchByAddress(address: String): Location?
    
    // Query by exact coordinates
    @Query("SELECT * FROM locations WHERE latitude = :latitude AND longitude = :longitude LIMIT 1")
    fun searchByCoordinates(latitude: Double, longitude: Double): Location?
    
    // Query by coordinates with tolerance (useful for approximate matches)
    @Query("""
        SELECT * FROM locations 
        WHERE ABS(latitude - :latitude) < :tolerance 
        AND ABS(longitude - :longitude) < :tolerance 
        LIMIT 1
    """)
    fun searchByCoordinatesWithTolerance(latitude: Double, longitude: Double, tolerance: Double = 0.001): Location?
    
    // Insert new location
    @Insert
    fun insert(location: Location): Long
    
    // Insert with full address
    fun insertByAddress(address: String, latitude: Double, longitude: Double): Long {
        return insert(Location(0, address, latitude, longitude))
    }
    
    // Update existing location by ID
    @Update
    fun update(location: Location)
    
    // Update by address (finds and updates)
    @Query("UPDATE locations SET latitude = :latitude, longitude = :longitude WHERE address LIKE '%' || :address || '%'")
    fun updateByAddress(address: String, latitude: Double, longitude: Double): Int
    
    // Update by coordinates (finds and updates address)
    @Query("UPDATE locations SET address = :address WHERE latitude = :latitude AND longitude = :longitude")
    fun updateByCoordinates(latitude: Double, longitude: Double, address: String): Int
    
    // Delete location
    @Delete
    fun delete(location: Location)
    
    // Delete by ID
    @Query("DELETE FROM locations WHERE id = :id")
    fun deleteById(id: Int)
    
    // Delete by address
    @Query("DELETE FROM locations WHERE address LIKE '%' || :address || '%'")
    fun deleteByAddress(address: String): Int
    
    // Delete by coordinates
    @Query("DELETE FROM locations WHERE latitude = :latitude AND longitude = :longitude")
    fun deleteByCoordinates(latitude: Double, longitude: Double): Int
    
    // Get all locations
    @Query("SELECT * FROM locations")
    fun getAll(): List<Location>
}
