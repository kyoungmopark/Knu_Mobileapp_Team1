package com.example.server.mapdata

import com.google.firebase.firestore.GeoPoint

data class MapData(
    val geoPoint: GeoPoint,
    val equipments: List<String>
) {
}