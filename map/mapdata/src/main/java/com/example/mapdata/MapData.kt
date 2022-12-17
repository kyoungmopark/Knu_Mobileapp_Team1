package com.example.mapdata

import com.google.firebase.firestore.GeoPoint

class MapData {
    lateinit var completeAddress: String
    var geoPoint: GeoPoint? = null
    lateinit var equipments: List<String>
}