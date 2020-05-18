/**
 * This app is part of an assessment of the module Mobile Applications in Dorset College
 * ID Student: 20325
 * Name: Bruno H. M. Mendanha
 */

package com.brunomendanha.googlemap

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_station.*

class StationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station)

        var intent = intent;

        var stationLatitude = intent.getStringExtra("lat")
        var stationLongitude = intent.getStringExtra("lng")
        var stationId = intent.getStringExtra("id")

        Log.i(getString(R.string.MAPLOGGING), stationLatitude)
        Log.i(getString(R.string.MAPLOGGING), stationLongitude)
        Log.i(getString(R.string.MAPLOGGING), stationId)

        Log.i(getString(R.string.MAPLOGGING), "intent called")

        textViewStationId.text = stationId
        textViewLatitude.text = stationLatitude
        textViewLongitude.text = stationLongitude

        buttonSavePreferences.setOnClickListener {
            saveAsFavourite("${stationId},${stationLatitude},${stationLongitude}")
        }
    }

    fun saveAsFavourite(markerText: String) {
        var prefs = getSharedPreferences("com.brunomendanha.googlemap", Context.MODE_PRIVATE)
        var markers = prefs.getStringSet("stationmarkers", setOf())?.toMutableSet()
        markers?.add(markerText)
        prefs.edit().putStringSet("stationmarkers", markers).apply()
        Log.i(getString(R.string.MAPLOGGING), "Marker Preferences are saved as ${markerText}")

       finish()
    }
}
