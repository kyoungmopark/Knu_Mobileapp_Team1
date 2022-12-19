package com.example.server

import android.util.Log
import com.example.server.data.DeserializedData
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass

// GSON을 사용하여 JSON을 역직렬화한다
class JsonDeserializer<T: DeserializedData>(private val clazz: KClass<T>, private val name: String) {
    companion object {
        private val gson by lazy { Gson() }
    }

    fun deserialize(json: String): List<T> {
        return try {
            gson.fromJson<List<T>>(
                JsonParser.parseString(json).asJsonObject.get("data"),
                TypeToken.getParameterized(List::class.java, clazz.java).type
            ).also {
                Log.d("knu", "succeed to deserialize json(size = ${it.size}) of $name")
            }
        } catch (e: Exception) {
            listOf<T>().also {
                Log.d("knu", "failed to deserialize json of $name\n$e")
            }
        }
    }
}