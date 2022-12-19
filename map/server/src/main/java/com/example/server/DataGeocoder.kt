package com.example.server

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.example.mapdata.MapData
import com.example.server.data.DeserializedData
import com.google.firebase.firestore.GeoPoint
import java.util.*

// 지오코더와 통신하여 데이터를 지오코딩하는 클래스
class DataGeocoder(context: Context) {
    private val geocoder = Geocoder(context, Locale.KOREA)

    private lateinit var onProgressListener: (Int, Int) -> Unit

    fun geocode(groupList: Map<String, List<DeserializedData>>): List<MapData> {
        return groupList.toList().mapIndexed { index, group ->
            MapData().apply {
                completeAddress = group.first

                geoPoint = try {
                    val address = geocoder.getFromLocationName(group.first, 1)[0]
                    GeoPoint(address.latitude, address.longitude).also {
                        Log.d("dev", "succeed to geocode ($completeAddress) -> $it")
                    }
                } catch (e: Exception) {
                    Log.d("dev", "failed to geocode ($completeAddress)")
                    null
                }

                equipments = group.second.mapNotNull {
                    it.getEquipmentsToList()
                }

                onProgressListener(index + 1, groupList.size)
            }
        }
    }

    fun setOnProgressListener(callback: (Int, Int) -> Unit) {
        onProgressListener = callback
    }
}