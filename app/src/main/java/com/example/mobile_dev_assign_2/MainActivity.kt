package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var searchInput: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnViewAll: Button
    
    private lateinit var database: AppDatabase
    private lateinit var locationDao: LocationDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        // Initialize database
        database = AppDatabase.getDatabase(this)
        locationDao = database.locationDao()

        // Initialize views
        btnSearch = findViewById(R.id.btnSearch)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnViewAll = findViewById(R.id.btnViewAll)

        // Set up button listeners
        btnSearch.setOnClickListener {
            handleSearch()
        }
        btnAdd.setOnClickListener {
            AddLocationActivity(this).show()
        }
        btnUpdate.setOnClickListener {
            UpdateLocationActivity(this).show()
        }
        btnDelete.setOnClickListener {
            DeleteLocationActivity(this).show()
        }
        btnViewAll.setOnClickListener {
            ViewAllActivity(this, locationDao).show()
        }

    }
    private fun handleSearch() {
        SearchActivity(this).show { location ->
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                mMap.clear()
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(location.address)
                        .snippet("Lat: ${location.latitude}, Lon: ${location.longitude}")
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                Toast.makeText(this, "Location found!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No location found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Default zoom to Toronto
        val toronto = LatLng(43.6532, -79.3832)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 10f))
    }
}
