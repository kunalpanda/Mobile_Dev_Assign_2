package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import androidx.room.Room

class DeleteLocationActivity(private val context: Context) {

    private val locationDao = AppDatabase.getDatabase(context).locationDao()

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_delete_location, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        dialog.show()

        // Initialize Views
        val btnSearchToDelete = dialogView.findViewById<Button>(R.id.btnSearchToDelete)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnDeleteCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnDeleteConfirm)

        var selectedLocation: Location? = null

        // --- Smart Search Integration ---
        btnSearchToDelete.setOnClickListener {
            SearchActivity(context).show { location ->
                if (location != null) {
                    selectedLocation = location
                    Toast.makeText(
                        context,
                        "Found ${location.address}\nReady for deletion",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, "No location found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // --- Cancel Button ---
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // --- Confirm Delete Button ---
        btnConfirm.setOnClickListener {
            if (selectedLocation == null) {
                Toast.makeText(context, "Search a location first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Confirm second time (extra safety)
            AlertDialog.Builder(context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete '${selectedLocation!!.address}'?")
                .setPositiveButton("Delete") { _, _ ->
                    try {
                        locationDao.delete(selectedLocation!!)
                        Toast.makeText(context, "Location deleted successfully", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to delete record", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
