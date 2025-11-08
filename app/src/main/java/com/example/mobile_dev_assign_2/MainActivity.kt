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
        initializeViews()
        
        // Set up button listeners
        setupButtonListeners()

    }
    
    private fun initializeViews() {

        searchInput = findViewById(R.id.searchInput)
        btnSearch = findViewById(R.id.btnSearch)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnViewAll = findViewById(R.id.btnViewAll)
    }
    
    private fun setupButtonListeners() {
        btnSearch.setOnClickListener {
            handleSearch()
        }
        btnAdd.setOnClickListener {
            AddLocationActivity(this, locationDao).show()
        }
        btnUpdate.setOnClickListener {
            UpdateLocationActivity(this, locationDao).show()
        }
        btnDelete.setOnClickListener {
            DeleteLocationActivity(this, locationDao).show()
        }
        btnViewAll.setOnClickListener {
            ViewAllActivity(this, locationDao).show()
        }

    }
    
    private fun handleSearch() {
        val address = searchInput.text.toString().trim()
        
        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Search database
        val location = locationDao.searchByAddress(address)
        
        if (location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            mMap.clear()

            // Create a marker with a title and snippet (popup content)
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(location.address)
                    .snippet("Lat: ${location.latitude}, Lon: ${location.longitude}")
            )

            // Move and zoom the camera to the marker
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))

            // Automatically show the info window
            marker?.showInfoWindow()

            Toast.makeText(this, "Location found!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Default zoom to Toronto
        val toronto = LatLng(43.6532, -79.3832)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 10f))
    }

}
