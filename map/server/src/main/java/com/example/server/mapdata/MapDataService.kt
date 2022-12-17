package com.example.server.mapdata

import android.content.Context
import com.example.server.rawdata.RawData

class MapDataService(context: Context) {
    private val dataGeocoder = DataGeocoder(context)
    private val firebaseUploader = FirebaseUploader()

    fun upload(name: String, rawDataGroupList: Map<String, List<RawData>>) {
        if (firebaseUploader.isRequired(name, rawDataGroupList.size)) {
            val mapDataList = dataGeocoder.convert(rawDataGroupList)
            firebaseUploader.update(name, mapDataList)
        }
    }
}