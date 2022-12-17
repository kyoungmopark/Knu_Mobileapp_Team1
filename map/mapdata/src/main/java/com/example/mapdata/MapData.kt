package com.example.mapdata

import com.google.firebase.firestore.GeoPoint

data class MapData(
    val completeAddress: String,
    val geoPoint: GeoPoint,
    val equipments: List<String>
) {
}