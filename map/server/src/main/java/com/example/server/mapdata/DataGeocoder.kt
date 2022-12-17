package com.example.server.mapdata

import android.content.Context
import android.location.Geocoder
import com.example.mapdata.MapData
import com.example.server.rawdata.RawData
import com.google.firebase.firestore.GeoPoint
import java.util.*

class DataGeocoder(context: Context) {
    val geocoder = Geocoder(context, Locale.KOREA)

    fun convert(rawDataGroupList: Map<String, List<RawData>>) =
        rawDataGroupList.map {
            MapData().apply {
                completeAddress = it.key

                geoPoint = try {
                    val address = geocoder.getFromLocationName(it.key, 1)[0]
                    GeoPoint(address.latitude, address.longitude)
                } catch (e: Exception) {
                    null
                }

                equipments = it.value.mapNotNull {
                    it.getEquipmentsToList()
                }
            }
        }
}