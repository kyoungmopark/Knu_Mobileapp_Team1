package com.example.server

import android.location.Geocoder
import android.util.Log
import com.example.server.data.DeserializedData
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

// 데이터 업데이트를 총괄한다 (Facade 패턴 활용)
class DataUpdater<T: DeserializedData>(
    clazz: KClass<T>, private val name: String,
    jsonUrl: String, jsonKey: String,
    geocoder: Geocoder
) {
    private val downloader = JsonDownloader(name, "$jsonUrl?perPage=0&serviceKey=$jsonKey")
    private val deserializer = JsonDeserializer(clazz, name)
    private val geocoder = DataGeocoder(name, geocoder)
    private val uploader = FirebaseUploader(name)

    private lateinit var onStartListener: (CoroutineScope) -> Unit
    private lateinit var onCompleteListener: (CoroutineScope) -> Unit

    suspend fun update(coroutine: CoroutineScope) {
        onStartListener(coroutine)
        val json = downloader.download()
        val deserializedDataList = deserializer.deserialize(json)
        val groupList = deserializedDataList.groupBy { it.getCompleteAddress() }

        if (isRequired(groupList.size)) {
            val mapDataList = geocoder.geocode(groupList, coroutine)
            uploader.upload(mapDataList)
        }
        onCompleteListener(coroutine)
    }

    private suspend fun isRequired(size: Int): Boolean {
        return (uploader.getTotal() != size).also {
            if (it) {
                Log.d("knu", "required to update data(size = $size) of $name")
            } else {
                Log.d("knu", "passed to update data(size = $size) of $name")
            }
        }
    }

    fun setOnStartListener(callback: (CoroutineScope) -> Unit) {
        onStartListener = callback
    }
    fun setOnCompleteListener(callback: (CoroutineScope) -> Unit) {
        onCompleteListener = callback
    }

    fun setOnGeocodeStartListener(callback: (Int, CoroutineScope) -> Unit) {
        geocoder.setOnStartListener(callback)
    }
    fun setOnGeocodeProgressListener(callback: (Int, CoroutineScope) -> Unit) {
        geocoder.setOnProgressListener(callback)
    }
    fun setOnGeocodeCompleteListener(callback: (Int, CoroutineScope) -> Unit) {
        geocoder.setOnCompleteListener(callback)
    }
}