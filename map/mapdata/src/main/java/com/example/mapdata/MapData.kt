package com.example.mapdata

import com.google.firebase.firestore.GeoPoint

class MapData() {
    lateinit var completeAddress: String
    var geoPoint: GeoPoint? = null
    lateinit var equipments: List<String>

    constructor(completeAddress: String, geoPoint: GeoPoint?, equipments: List<String>) : this() {
        this.completeAddress = completeAddress
        this.geoPoint = geoPoint
        this.equipments = equipments
    }

    override fun toString() = "MapData($completeAddress, $geoPoint)"
}