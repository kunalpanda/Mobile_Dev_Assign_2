package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import androidx.room.Room

class UpdateLocationActivity(private val context: Context) {

    private val locationDao = AppDatabase.getDatabase(context).locationDao()

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_update_location, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        dialog.show()

        // Views
        val btnSearchLocation = dialogView.findViewById<Button>(R.id.btnSearchLocation)
        val addressInput = dialogView.findViewById<EditText>(R.id.dialogUpdateAddress)
        val latitudeInput = dialogView.findViewById<EditText>(R.id.dialogUpdateLatitude)
        val longitudeInput = dialogView.findViewById<EditText>(R.id.dialogUpdateLongitude)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnUpdateCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnUpdateConfirm)

        var selectedLocation: Location? = null // holds the current record to update

        // --- Smart Search Button ---
        btnSearchLocation.setOnClickListener {
            SearchActivity(context).show { location ->
                if (location != null) {
                    selectedLocation = location
                    addressInput.setText(location.address)
                    latitudeInput.setText(location.latitude.toString())
                    longitudeInput.setText(location.longitude.toString())
                    Toast.makeText(context, "Loaded location for update", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No location found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // --- Cancel Button ---
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // --- Confirm Update Button ---
        btnConfirm.setOnClickListener {
            if (selectedLocation == null) {
                Toast.makeText(context, "Search a location first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newAddress = addressInput.text.toString().trim()
            val latStr = latitudeInput.text.toString().trim()
            val lonStr = longitudeInput.text.toString().trim()

            if (newAddress.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val newLatitude = latStr.toDouble()
                val newLongitude = lonStr.toDouble()

                val updatedLocation = selectedLocation!!.copy(
                    address = newAddress,
                    latitude = newLatitude,
                    longitude = newLongitude
                )

                locationDao.update(updatedLocation)
                Toast.makeText(context, "Location updated successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Invalid latitude or longitude", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
