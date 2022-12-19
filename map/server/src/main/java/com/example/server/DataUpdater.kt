package com.example.server

import android.content.Context
import android.util.Log
import com.example.server.data.DeserializedData
import kotlin.reflect.KClass

// 데이터를 업데이트하는 facade다.
class DataUpdater<T: DeserializedData>(
    clazz: KClass<T>, name: String,
    jsonUrl: String, jsonKey: String,
    context: Context
) {
    private val downloader = JsonDownloader("$jsonUrl?perPage=0&serviceKey=$jsonKey")
    private val deserializer = JsonDeserializer(clazz)
    private val geocoder = DataGeocoder(context)
    private val uploader = FirebaseUploader(name)

    suspend fun update() {
        val json = downloader.download()
        val deserializedDataList = deserializer.deserialize(json)
        val rawDataGroups = deserializedDataList.groupBy { it.getCompleteAddress() }

        if (isRequired(rawDataGroups.size)) {
            val mapDataList = geocoder.geocode(rawDataGroups)
            uploader.upload(mapDataList)
        }
    }

    private suspend fun isRequired(size: Int): Boolean {
        return (uploader.getTotal() != size).also {
            if (it) {
                Log.d("dev", "update required")
            } else {
                Log.d("dev", "update passed")
            }
        }
    }

    fun setOnGeocodeProgressListener(callback: (Int, Int) -> Unit) {
        geocoder.setOnProgressListener(callback)
    }

    fun setOnUploadCompletedListener(callback: () -> Unit) {
        uploader.setOnCompletedListener(callback)
    }
}