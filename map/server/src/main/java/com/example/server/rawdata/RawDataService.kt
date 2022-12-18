package com.example.server.rawdata

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass

class RawDataService<T: RawData>(private val clazz: KClass<T>, urlBase: String, serviceKey: String) {
    private val downloader = JsonDownloader("$urlBase?perPage=0&serviceKey=$serviceKey")

    fun receive(): List<T> {
        return downloader.receive().deserialize()
    }
    
    private fun String.deserialize(): List<T> {
        return try {
            gson.fromJson<List<T>>(
                JsonParser.parseString(this).asJsonObject.get("data"),
                TypeToken.getParameterized(List::class.java, clazz.java).type
            ).also {
                Log.d("dev", "succeed to deserialize")
            }
        } catch (e: Exception) {
            Log.d("dev", "failed to deserialize")
            listOf()
        }
    }

    companion object {
        private val gson by lazy { Gson() }
    }
}