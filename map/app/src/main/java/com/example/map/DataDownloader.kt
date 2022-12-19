package com.example.map

import com.example.mapdata.MapData

// 데이터 다운로드를 총괄한다
class DataDownloader(name: String) {
    private val downloader =  FirebaseDownloader(name)

    suspend fun download(): List<MapData> {
        return downloader.download()
    }
}