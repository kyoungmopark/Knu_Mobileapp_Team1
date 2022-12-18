package com.example.server.rawdata

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass

class RawDataService<T: RawData>(private val clazz: KClass<T>, urlBase: String, serviceKey: String) {
    private val downloader = JsonDownloader("$urlBase?perPage=0&serviceKey=$serviceKey")

    fun receive() = downloader.receive().deserialize()
    
    private fun String.deserialize(): List<T> {
        return gson.fromJson(
            JsonParser.parseString(this).asJsonObject.get("data"),
            TypeToken.getParameterized(List::class.java, clazz.java).type
        )
    }

    companion object {
        private val gson by lazy {
            Gson()
        }
    }
}