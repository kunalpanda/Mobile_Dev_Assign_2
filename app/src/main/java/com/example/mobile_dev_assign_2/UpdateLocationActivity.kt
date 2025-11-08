package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UpdateLocationActivity(
    private val context: Context,
    private val locationDao: LocationDao
) {
    fun show() {
        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.dialog_update)
            .create()
        dialog.show()

        val searchInput = dialog.findViewById<EditText>(R.id.dialogUpdateSearch)
        val addressInput = dialog.findViewById<EditText>(R.id.dialogUpdateAddress)
        val latitudeInput = dialog.findViewById<EditText>(R.id.dialogUpdateLatitude)
        val longitudeInput = dialog.findViewById<EditText>(R.id.dialogUpdateLongitude)
        val btnCancel = dialog.findViewById<Button>(R.id.btnUpdateCancel)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnUpdateConfirm)

        btnCancel?.setOnClickListener { dialog.dismiss() }

        btnConfirm?.setOnClickListener {
            val searchAddress = searchInput?.text.toString().trim()
            val newAddress = addressInput?.text.toString().trim()
            val latStr = latitudeInput?.text.toString().trim()
            val lonStr = longitudeInput?.text.toString().trim()

            if (searchAddress.isEmpty() || newAddress.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val location = locationDao.searchByAddress(searchAddress)
            if (location != null) {
                try {
                    val updated = location.copy(
                        address = newAddress,
                        latitude = latStr.toDouble(),
                        longitude = lonStr.toDouble()
                    )
                    locationDao.update(updated)
                    Toast.makeText(context, "Location updated successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Invalid latitude or longitude", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
