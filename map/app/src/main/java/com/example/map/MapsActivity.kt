package com.example.map

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.map.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import data.DaeguBukguService
import data.DaeguJungguService
import java.util.*
import kotlin.concurrent.thread

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        thread(true) {
            for (entry in DaeguBukguService().receive()) {
                val address = Geocoder(this, Locale.KOREA).getFromLocationName(entry.key, 1)[0]
                runOnUiThread {
                    mMap.addMarker(MarkerOptions().position(LatLng(address.latitude, address.longitude)))
                }
            }
        }
        thread(true) {
            for (entry in DaeguJungguService().receive()) {
                val address = Geocoder(this, Locale.KOREA).getFromLocationName(entry.key, 1)[0]
                runOnUiThread {
                    mMap.addMarker(MarkerOptions().position(LatLng(address.latitude, address.longitude)))
                }
            }
        }
        /*
        val locations = mapOf<String,LatLng>("글로벌프라자" to LatLng(35.8896997627053,128.61168939232567),"북문" to LatLng(35.889070663727615,128.60887490090266))

        for ((key,value) in locations) {
            mMap.addMarker(MarkerOptions().position(value).title("${key}"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(value))
        }
        */
    }
}