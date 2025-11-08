package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class DeleteLocationActivity(
    private val context: Context,
    private val locationDao: LocationDao
) {
    fun show() {
        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.dialog_delete)
            .create()
        dialog.show()

        val searchInput = dialog.findViewById<EditText>(R.id.dialogDeleteSearch)
        val btnCancel = dialog.findViewById<Button>(R.id.btnDeleteCancel)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnDeleteConfirm)

        btnCancel?.setOnClickListener { dialog.dismiss() }

        btnConfirm?.setOnClickListener {
            val address = searchInput?.text.toString().trim()
            if (address.isEmpty()) {
                Toast.makeText(context, "Please enter an address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val location = locationDao.searchByAddress(address)
            if (location != null) {
                locationDao.delete(location)
                Toast.makeText(context, "Location deleted successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
