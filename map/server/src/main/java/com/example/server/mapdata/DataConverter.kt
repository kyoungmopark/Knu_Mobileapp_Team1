package com.example.server.mapdata

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.example.mapdata.MapData
import com.example.server.rawdata.RawData
import com.google.firebase.firestore.GeoPoint
import java.util.*

class DataConverter(context: Context) {
    private val geocoder = Geocoder(context, Locale.KOREA)

    fun convert(rawDataGroupList: Map<String, List<RawData>>): List<MapData> {
        return rawDataGroupList.map {
            MapData().apply {
                completeAddress = it.key

                geoPoint = try {
                    val address = geocoder.getFromLocationName(it.key, 1)[0]
                    GeoPoint(address.latitude, address.longitude).also {
                        Log.d("dev", "succeed to convert $completeAddress -> $it")
                    }
                } catch (e: Exception) {
                    Log.d("dev", "failed to convert (${e.message})")
                    null
                }

                equipments = it.value.mapNotNull {
                    it.getEquipmentsToList()
                }
            }
        }
    }
}