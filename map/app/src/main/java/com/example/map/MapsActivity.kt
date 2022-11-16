package com.example.map

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.map.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import rawdata.DaeguBukguData
import rawdata.RawDataService
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

        CoroutineScope(Dispatchers.IO).launch {
            val service = RawDataService(DaeguBukguData::class, "https://api.odcloud.kr/api/15101420/v1/uddi:37a2f399-4dd0-4483-8c3a-97c81e7171c8?perPage=0&serviceKey=N5y%2B8eZU60ZCyJtY9JpQwgjexI5cM5LAK8S4s1p3WHgtTMXy24R4z%2Bt7njRjWjXdjVwteW39U5SPpLsLcAnB%2Fg%3D%3D")
            val dataList = service.receive()
            val geocoder = Geocoder(this@MapsActivity, Locale.KOREA)

            for (data in dataList) {
                try {
                    Log.d("SIN", "${data.address}")

                    val address = geocoder.getFromLocationName(data.address, 1)[0]

                    withContext(Dispatchers.Main) {
                        mMap.addMarker(MarkerOptions().position(LatLng(address.latitude, address.longitude)))
                    }

                } catch (e: Exception) {
                    Log.d("SIN", e.toString())
                }
            }
        }
    }
}