package com.example.mobile_dev_assign_2

import android.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.TextView

class ViewAllActivity(
    private val context: Context,
    private val locationDao: LocationDao
) {
    fun show() {
        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.activity_view_all)
            .create()
        dialog.show()

        val allLocationsText = dialog.findViewById<TextView>(R.id.allLocationsText)
        val btnClose = dialog.findViewById<Button>(R.id.btnClose)

        val locations = locationDao.getAll()
        if (locations.isEmpty()) {
            allLocationsText?.text = "No locations in database"
        } else {
            val locationsText = StringBuilder()
            locations.forEachIndexed { index, loc ->
                locationsText.append("${index + 1}. ${loc.address}\n")
                locationsText.append("   Lat: ${loc.latitude}, Lon: ${loc.longitude}\n\n")
            }
            allLocationsText?.text = locationsText.toString()
        }

        btnClose?.setOnClickListener { dialog.dismiss() }
    }
}
