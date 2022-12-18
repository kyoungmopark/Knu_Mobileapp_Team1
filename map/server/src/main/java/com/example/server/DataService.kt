package com.example.server

import com.example.server.mapdata.DataConverter
import com.example.server.mapdata.MapDataService
import com.example.server.rawdata.RawData
import com.example.server.rawdata.RawDataService

class DataService<T: RawData>(
    private val rawDataService: RawDataService<T>,
    private val converter: DataConverter,
    private val mapDataService: MapDataService
) {
    suspend fun update() {
        val groupingList = rawDataService.receive().groupBy { it.getCompleteAddress() }

        if (mapDataService.isRequiredUpdate(groupingList.size)) {
            val mapDataList = converter.convert(groupingList)
            mapDataService.update(mapDataList)
        }
    }
}