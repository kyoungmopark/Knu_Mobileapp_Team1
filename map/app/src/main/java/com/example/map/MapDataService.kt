package com.example.map

import android.util.Log
import com.example.mapdata.MapData
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MapDataService (
    var db : FirebaseFirestore,
    var dataname : String) {
    fun getData(): MutableList<MapData> {
        val firebaseMapDataList = mutableListOf<MapData>()

        db.collection(dataname)
            .get().addOnSuccessListener { document ->
                for(d in document) {
                    if (!(d.id.equals("total"))) {
                        Log.d("errorcheck", "${d.id}")
                        val tempMapData = d.toObject(MapData::class.java)
                        firebaseMapDataList.add(tempMapData)
                        Log.d("errorcheck", "${firebaseMapDataList.last().completeAddress}")

                        /*val lat: Double? = tempMapData.geoPoint?.getLatitude()
                        val lng: Double? = tempMapData.geoPoint?.getLongitude()

                        if ((lat != null) && (lng != null)){
                            val markerOptions = MarkerOptions()
                            markerOptions.position(LatLng(lat, lng))
                            val marker = mMap.addMarker(markerOptions)

                            marker?.tag = tempMapData.completeAddress + "/"

                            for (i in tempMapData.equipments) {
                                marker?.tag = marker?.tag.toString() + "$i \n"
                            }
                        }*/
                    }
                }
            }

        return firebaseMapDataList
    }
}

