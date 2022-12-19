package com.example.server

import android.location.Geocoder
import android.util.Log
import com.example.mapdata.MapData
import com.example.server.data.DeserializedData
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

// 지오코더를 사용하여 데이터를 지오코딩한다
class DataGeocoder(private val name: String, geocoder: Geocoder) {
    private val geocoder = geocoder

    private lateinit var onStartListener: (Int) -> Unit
    private lateinit var onProgressListener: (Int) -> Unit
    private lateinit var onCompleteListener: (Int) -> Unit

    fun geocode(groupList: Map<String, List<DeserializedData>>): List<MapData> {
        //onStartListener(groupList.size)
        var progress = 0

        return groupList.map { group ->
            MapData(
                completeAddress = group.key,
                geoPoint = try {
                    val address = geocoder.getFromLocationName(group.key, 1)[0]
                    GeoPoint(address.latitude, address.longitude).also {
                        Log.d("knu", "succeed to geocode data($name\n${group.key} -> $it) of $name")
                    }
                } catch (e: Exception) {
                    Log.d("knu", "failed to geocode data(${group.key}) of $name\n$e")
                    null
                },
                equipments = group.value.mapNotNull {
                    it.getEquipmentsToList()
                }
            ).also {
                progress += 1
                //onProgressListener(progress)
            }
        }.also {
            //onCompleteListener(it.size)
        }
    }

    fun setOnStartListener(callback: (Int) -> Unit) {
        onStartListener = callback
    }
    fun setOnProgressListener(callback: (Int) -> Unit) {
        onProgressListener = callback
    }
    fun setOnCompleteListener(callback: (Int) -> Unit) {
        onCompleteListener = callback
    }
}