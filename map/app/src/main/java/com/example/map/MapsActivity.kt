package com.example.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.map.databinding.ActivityMapsBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    lateinit var providerClient: FusedLocationProviderClient
    lateinit var apiClient: GoogleApiClient
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var bukguDataDownloader: DataDownloader
    private lateinit var jungguDataDownloader: DataDownloader
    private lateinit var suseongguDataDownloader: DataDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (it.all { permission -> permission.value == true }) {
                // ?????? ????????? ????????????
                apiClient.connect()
            } else {
                Toast.makeText(this, "?????? ??????", Toast.LENGTH_SHORT).show()
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        providerClient = LocationServices.getFusedLocationProviderClient(this)
        apiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NETWORK_STATE
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE
                )
            )
        } else {
            // ?????? ????????? ????????????
            apiClient.connect()
        }
        binding.cardView.visibility = View.GONE

        bukguDataDownloader = DataDownloader(getString(R.string.bukgu))
        jungguDataDownloader = DataDownloader(getString(R.string.junggu))
        suseongguDataDownloader = DataDownloader(getString(R.string.suseonggu))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 &&
            grantResults[0] === PackageManager.PERMISSION_GRANTED &&
            grantResults[1] === PackageManager.PERMISSION_GRANTED &&
            grantResults[2] === PackageManager.PERMISSION_GRANTED
        ) {
            apiClient.connect()
        }
    }

    private fun moveMap(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        val position: CameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        // ?????? ?????? ????????????
        mMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        // ?????? ??????
        val markerOptions = MarkerOptions()
        markerOptions.icon(
            BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE
            )
        )
        markerOptions.position(latLng)
        // ?????? ????????????
        mMap?.addMarker(markerOptions)?.tag = "????????????/ "

        binding.currentLocationBtn.setOnClickListener {
            mMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        }
    }

    // ?????? ???????????? ????????? ??? ?????? ????????? ???
    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            providerClient.getLastLocation().addOnSuccessListener(
                this@MapsActivity,
                object : OnSuccessListener<Location> {
                    override fun onSuccess(location: Location?) {
                        location?.let {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            // ?????? ?????? ????????????
                            moveMap(latitude, longitude)
                        }
                    }
                })
            apiClient.disconnect()
        }
    }
    override fun onConnectionSuspended(p0: Int) {
        // ?????? ???????????? ????????? ??? ?????? ???
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        // ????????? ??? ?????? ?????? ???????????? ?????? ???
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        CoroutineScope(Dispatchers.IO).launch {
            val mapDataList = bukguDataDownloader.download()
            mapDataList.forEach { mapData ->
                Log.d("knu", "get $mapData of bukgu")
                mapData.geoPoint?.also { geoPoint ->
                    withContext(Dispatchers.Main) {
                        val latLng = LatLng(geoPoint.latitude, geoPoint.longitude)
                        val markerOptions = MarkerOptions().apply { position(latLng) }
                        mMap.addMarker(markerOptions)?.apply {
                            tag = "${mapData.completeAddress}/${mapData.equipments.joinToString("\n")}"
                        }
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val mapDataList = jungguDataDownloader.download()
            mapDataList.forEach { mapData ->
                Log.d("knu", "get $mapData of junggu")
                mapData.geoPoint?.also { geoPoint ->
                    withContext(Dispatchers.Main) {
                        val latLng = LatLng(geoPoint.latitude, geoPoint.longitude)
                        val markerOptions = MarkerOptions().apply { position(latLng) }
                        mMap.addMarker(markerOptions)?.apply {
                            tag = "${mapData.completeAddress}/${mapData.equipments.joinToString("\n")}"
                        }
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val mapDataList = suseongguDataDownloader.download()
            mapDataList.forEach { mapData ->
                Log.d("knu", "get $mapData of suseonggu")
                mapData.geoPoint?.also { geoPoint ->
                    withContext(Dispatchers.Main) {
                        val latLng = LatLng(geoPoint.latitude, geoPoint.longitude)
                        val markerOptions = MarkerOptions().apply { position(latLng) }
                        mMap.addMarker(markerOptions)?.apply {
                            tag = "${mapData.completeAddress}/${mapData.equipments.joinToString("\n")}"
                        }
                    }
                }
            }
        }

        // ?????? ?????? ????????? : ???????????? ???????????? ??????
        googleMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(marker: Marker): Boolean {
                binding.cardView.visibility = View.VISIBLE
                var addr = findViewById<TextView>(R.id.equipment_addr)
                var type = findViewById<TextView>(R.id.equipment_type)
                var arr = marker.tag.toString().split("/")

                addr.text = arr[0]
                type.text = arr[1]
                Log.d("errorcheck", "${arr[1]}")

                return false
            }
        })

        // ??? ?????? ????????? : ???????????? ????????? ?????????
        googleMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                binding.cardView.visibility = View.GONE
            }
        })
    }
}