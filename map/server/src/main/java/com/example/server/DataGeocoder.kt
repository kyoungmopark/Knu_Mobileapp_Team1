package com.example.server

import android.location.Geocoder
import android.util.Log
import com.example.mapdata.MapData
import com.example.server.data.DeserializedData
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

// 지오코더를 사용하여 데이터를 지오코딩한다
class DataGeocoder(private val name: String, geocoder: Geocoder) {
    private val geocoder = geocoder

    private lateinit var onStartListener: (Int, CoroutineScope) -> Unit
    private lateinit var onProgressListener: (Int, CoroutineScope) -> Unit
    private lateinit var onCompleteListener: (Int, CoroutineScope) -> Unit

    fun geocode(
        groupList: Map<String, List<DeserializedData>>,
        coroutine: CoroutineScope
    ): List<MapData> {
        onStartListener(groupList.size, coroutine)
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
                onProgressListener(progress, coroutine)
            }
        }.also {
            onCompleteListener(it.size, coroutine)
        }
    }

    fun setOnStartListener(callback: (Int, CoroutineScope) -> Unit) {
        onStartListener = callback
    }
    fun setOnProgressListener(callback: (Int, CoroutineScope) -> Unit) {
        onProgressListener = callback
    }
    fun setOnCompleteListener(callback: (Int, CoroutineScope) -> Unit) {
        onCompleteListener = callback
    }
}