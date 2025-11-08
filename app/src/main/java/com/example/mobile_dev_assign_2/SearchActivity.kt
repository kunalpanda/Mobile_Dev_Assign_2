package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import kotlin.math.abs

class SearchActivity(
    private val context: Context,
    private val locationDao: LocationDao
) {
    fun show(onResult: (Location?) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_search, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        dialog.show()

        val searchTypeSpinner = dialogView.findViewById<Spinner>(R.id.spinnerSearchType)
        val inputAddress = dialogView.findViewById<EditText>(R.id.searchAddressInput)
        val inputLatitude = dialogView.findViewById<EditText>(R.id.searchLatitudeInput)
        val inputLongitude = dialogView.findViewById<EditText>(R.id.searchLongitudeInput)
        val btnSearch = dialogView.findViewById<Button>(R.id.btnPerformSearch)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelSearch)
        val resultText = dialogView.findViewById<TextView>(R.id.txtSearchResult)

        // Populate search type spinner
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            listOf("By Address", "By Coordinates")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchTypeSpinner.adapter = adapter

        // Hide coordinate fields by default
        inputLatitude.visibility = EditText.GONE
        inputLongitude.visibility = EditText.GONE

        searchTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (position == 0) {
                    inputAddress.visibility = EditText.VISIBLE
                    inputLatitude.visibility = EditText.GONE
                    inputLongitude.visibility = EditText.GONE
                } else {
                    inputAddress.visibility = EditText.GONE
                    inputLatitude.visibility = EditText.VISIBLE
                    inputLongitude.visibility = EditText.VISIBLE
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnSearch.setOnClickListener {
            val selected = searchTypeSpinner.selectedItemPosition
            var foundLocation: Location? = null

            try {
                foundLocation = if (selected == 0) {
                    val addr = inputAddress.text.toString().trim()
                    if (addr.isEmpty()) {
                        Toast.makeText(context, "Enter address to search", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    locationDao.searchByAddress(addr)
                } else {
                    val lat = inputLatitude.text.toString().toDouble()
                    val lon = inputLongitude.text.toString().toDouble()
                    locationDao.searchByCoordinatesWithTolerance(lat, lon, 0.001)
                }

                if (foundLocation != null) {
                    resultText.text = "Found: ${foundLocation.address}\nLat: ${foundLocation.latitude}, Lon: ${foundLocation.longitude}"
                } else {
                    resultText.text = "No matching location found."
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Invalid input format", Toast.LENGTH_SHORT).show()
            }

            onResult(foundLocation)
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}
