package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import androidx.room.Room

class AddLocationActivity(private val context: Context) {

    private val locationDao = AppDatabase.getDatabase(context).locationDao()

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_add_location, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        dialog.show()

        // --- Initialize Views ---
        val addressInput = dialogView.findViewById<EditText>(R.id.dialogAddAddress)
        val latitudeInput = dialogView.findViewById<EditText>(R.id.dialogAddLatitude)
        val longitudeInput = dialogView.findViewById<EditText>(R.id.dialogAddLongitude)
        val btnSearchExisting = dialogView.findViewById<Button>(R.id.btnSearchExisting)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnAddCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnAddConfirm)

        var existingLocation: Location? = null

        // --- Search Existing Button ---
        btnSearchExisting.setOnClickListener {
            SearchActivity(context).show { location ->
                if (location != null) {
                    existingLocation = location
                    addressInput.setText(location.address)
                    latitudeInput.setText(location.latitude.toString())
                    longitudeInput.setText(location.longitude.toString())
                    Toast.makeText(context, "Existing location loaded", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No existing record found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // --- Cancel Button ---
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // --- Confirm Add Button ---
        btnConfirm.setOnClickListener {
            val address = addressInput.text.toString().trim()
            val latStr = latitudeInput.text.toString().trim()
            val lonStr = longitudeInput.text.toString().trim()

            if (address.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val latitude = latStr.toDouble()
                val longitude = lonStr.toDouble()

                // Check for duplicate before inserting
                val duplicate = locationDao.searchByCoordinatesWithTolerance(latitude, longitude)
                    ?: locationDao.searchByAddress(address)

                if (duplicate != null) {
                    Toast.makeText(
                        context,
                        "A location with similar details already exists.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                locationDao.insert(Location(0, address, latitude, longitude))
                Toast.makeText(context, "Location added successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()

            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Invalid latitude or longitude", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error adding location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
