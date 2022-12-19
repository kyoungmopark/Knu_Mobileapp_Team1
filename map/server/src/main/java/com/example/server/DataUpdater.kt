package com.example.server

import android.location.Geocoder
import android.util.Log
import com.example.server.data.DeserializedData
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

    private lateinit var onStartListener: () -> Unit
    private lateinit var onCompleteListener: () -> Unit

    suspend fun update() {
        onStartListener()
        val json = downloader.download()
        val deserializedDataList = deserializer.deserialize(json)
        val groupList = deserializedDataList.groupBy { it.getCompleteAddress() }

        if (isRequired(groupList.size)) {
            val mapDataList = geocoder.geocode(groupList)
            uploader.upload(mapDataList)
        }
        onCompleteListener()
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

    fun setOnStartListener(callback: () -> Unit) {
        onStartListener = callback
    }
    fun setOnCompleteListener(callback: () -> Unit) {
        onCompleteListener = callback
    }

    fun setOnGeocodeStartListener(callback: (Int) -> Unit) {
        geocoder.setOnStartListener(callback)
    }
    fun setOnGeocodeProgressListener(callback: (Int) -> Unit) {
        geocoder.setOnProgressListener(callback)
    }
    fun setOnGeocodeCompleteListener(callback: (Int) -> Unit) {
        geocoder.setOnCompleteListener(callback)
    }
}