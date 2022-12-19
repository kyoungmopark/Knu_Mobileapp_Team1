package com.example.map

import android.content.Context
import android.util.Log
import com.example.mapdata.MapData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// 파이어베이스로부터 데이터를 다운로드받는다
class FirebaseDownloader(private val name: String) {
    companion object {
        private val firestore by lazy { FirebaseFirestore.getInstance() }
    }

    private val collection = firestore.collection(name)

    suspend fun download(): List<MapData> {
        val task = collection.get()
        val query = task.await()

        return if (task.isSuccessful) {
            query.documents.map { document ->
                document.toObject(MapData::class.java) ?: MapData()
            }.also {
                Log.d("knu", "succeed to download data(size = ${it.size}) from $name")
            }
        } else {
            Log.d("knu", "failed to download data from $name")
            listOf()
        }
    }
}

