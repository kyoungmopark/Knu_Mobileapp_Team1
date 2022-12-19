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
            Log.d("dev", "succeed to download data from $name")
            query.documents.filterNot { it.id != "total" }.map { document ->
                document.toObject(MapData::class.java) ?: MapData()
            }
        } else {
            Log.d("dev", "failed to download data from $name")
            listOf()
        }
    }
}

