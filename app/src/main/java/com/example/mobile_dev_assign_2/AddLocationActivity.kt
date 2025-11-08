package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddLocationActivity(
    private val context: Context,
    private val locationDao: LocationDao
) {
    fun show() {
        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.dialog_add)
            .create()
        dialog.show()

        val addressInput = dialog.findViewById<EditText>(R.id.dialogAddAddress)
        val latitudeInput = dialog.findViewById<EditText>(R.id.dialogAddLatitude)
        val longitudeInput = dialog.findViewById<EditText>(R.id.dialogAddLongitude)
        val btnCancel = dialog.findViewById<Button>(R.id.btnAddCancel)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnAddConfirm)

        btnCancel?.setOnClickListener { dialog.dismiss() }

        btnConfirm?.setOnClickListener {
            val address = addressInput?.text.toString().trim()
            val latStr = latitudeInput?.text.toString().trim()
            val lonStr = longitudeInput?.text.toString().trim()

            if (address.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val latitude = latStr.toDouble()
                val longitude = lonStr.toDouble()

                locationDao.insert(Location(0, address, latitude, longitude))
                Toast.makeText(context, "Location added successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Invalid latitude or longitude", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
