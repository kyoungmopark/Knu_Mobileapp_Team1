package com.example.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.map.databinding.ActivityMapsBinding
import com.example.mapdata.MapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val card_view =LayoutInflater.from(this).inflate(R.layout.datail_info, null)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val firebaseMapDataList = mutableListOf<MapData>()
        db = FirebaseFirestore.getInstance()

        val position = CameraPosition.Builder()
            .target(LatLng(35.893190, 128.610165)).zoom(16f).build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        db.collection("bukgu")
            .get().addOnSuccessListener { document ->
                for(d in document) {
                    Log.d("errorcheck", "${d.id}")
                    if (!(d.id.equals("0"))) {
                        val tempMapData = d.toObject(MapData::class.java)
                        firebaseMapDataList.add(tempMapData)

                        val lat: Double? = tempMapData.geoPoint?.getLatitude()
                        val lng: Double? = tempMapData.geoPoint?.getLongitude()

                        if ((lat != null) && (lng != null)){
                            mMap.addMarker(MarkerOptions().position(LatLng(lat, lng)))
                        }
                    }
                }
        }
        //mMap.addMarker(MarkerOptions().position(LatLng(address.latitude, address.longitude)))

    }

}