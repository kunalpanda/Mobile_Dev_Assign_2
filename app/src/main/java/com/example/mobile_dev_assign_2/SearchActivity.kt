package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.*
import androidx.room.Room

class SearchActivity(private val context: Context) {
    private val locationDao = AppDatabase.getDatabase(context).locationDao()

    fun show(onResult: (Location?) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_search, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        dialog.show()

        // --- Initialize views ---
        val spinnerSearchType = dialogView.findViewById<Spinner>(R.id.spinnerSearchType)
        val addressInput = dialogView.findViewById<EditText>(R.id.searchAddressInput)
        val latitudeInput = dialogView.findViewById<EditText>(R.id.searchLatitudeInput)
        val longitudeInput = dialogView.findViewById<EditText>(R.id.searchLongitudeInput)
        val txtResult = dialogView.findViewById<TextView>(R.id.txtSearchResult)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelSearch)
        val btnSearch = dialogView.findViewById<Button>(R.id.btnPerformSearch)

        // --- Populate spinner ---
        val searchTypes = listOf("Search by Address", "Search by Coordinates")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, searchTypes)
        spinnerSearchType.adapter = adapter

        // --- Switch input fields based on selection ---
        spinnerSearchType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                when (position) {
                    0 -> { // Address search
                        addressInput.visibility = android.view.View.VISIBLE
                        latitudeInput.visibility = android.view.View.GONE
                        longitudeInput.visibility = android.view.View.GONE
                    }
                    1 -> { // Coordinates search
                        addressInput.visibility = android.view.View.GONE
                        latitudeInput.visibility = android.view.View.VISIBLE
                        longitudeInput.visibility = android.view.View.VISIBLE
                    }
                }
                txtResult.text = "Result will appear here"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // --- Cancel button ---
        btnCancel.setOnClickListener {
            dialog.dismiss()
            onResult(null)
        }

        // --- Perform search ---
        btnSearch.setOnClickListener {
            val selectedType = spinnerSearchType.selectedItemPosition
            var found: Location? = null

            try {
                if (selectedType == 0) {
                    // Search by address
                    val address = addressInput.text.toString().trim()
                    if (address.isEmpty()) {
                        Toast.makeText(context, "Please enter an address", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    found = locationDao.searchByAddress(address)
                } else {
                    // Search by coordinates
                    val latStr = latitudeInput.text.toString().trim()
                    val lonStr = longitudeInput.text.toString().trim()
                    if (latStr.isEmpty() || lonStr.isEmpty()) {
                        Toast.makeText(context, "Please enter latitude and longitude", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val lat = latStr.toDouble()
                    val lon = lonStr.toDouble()
                    found = locationDao.searchByCoordinatesWithTolerance(lat, lon)
                }

                if (found != null) {
                    txtResult.text = "Found: ${found.address}\nLat: ${found.latitude}, Lon: ${found.longitude}"
                    Toast.makeText(context, "Location found!", Toast.LENGTH_SHORT).show()

                    // Auto-dismiss after 1 second
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                        onResult(found)
                    }, 1000)
                } else {
                    txtResult.text = "No matching location found."
                    Toast.makeText(context, "No location found", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                txtResult.text = "Invalid input. Please check values."
                Toast.makeText(context, "Error while searching", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
