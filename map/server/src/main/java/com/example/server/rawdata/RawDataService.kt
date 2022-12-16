package com.example.server.rawdata

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass

class RawDataService<T: RawData>(private val clazz: KClass<T>, urlString: String) {
    private val conn = JsonConn(urlString)
    private val gson = Gson()

    fun receive() = conn.receive().deserialize()
    fun receiveByGroup() = receive().groupBy { it.getCompleteAddress() }
    
    private fun String.deserialize(): List<T> =
        gson.fromJson(
            JsonParser.parseString(this).asJsonObject.get("data"),
            TypeToken.getParameterized(List::class.java, clazz.java).type
        )
}