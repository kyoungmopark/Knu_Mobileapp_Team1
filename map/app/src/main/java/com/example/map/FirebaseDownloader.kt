package com.example.map

import android.util.Log
import com.example.mapdata.MapData
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// 파이어베이스로부터 데이터를 다운로드받는다
class FirebaseDownloader(private val name: String) {
    companion object {
        private val firestore by lazy { FirebaseFirestore.getInstance() }
    }

    private val collection = firestore.collection(name)

    suspend fun download(): List<MapData> = suspendCoroutine { continuation ->
        collection.get().addOnSuccessListener { query ->
            val mapDataList = query.documents.map { document ->
                document.toObject(MapData::class.java) ?: MapData()
            }
            continuation.resume(mapDataList)
        }.addOnFailureListener {
            Log.d("knu", "failed to download data from $name\n$it")
            continuation.resume(listOf())
        }
    }
}

