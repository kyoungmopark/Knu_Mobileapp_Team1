package com.example.server

import android.util.Log
import com.example.mapdata.MapData
import com.example.mapdata.TotalData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// 파이어베이스에 데이터를 업로드한다
class FirebaseUploader(private val name: String) {
    companion object {
        private val firestore by lazy { FirebaseFirestore.getInstance() }
    }

    private val collection = firestore.collection(name)

    suspend fun getTotal(): Int = suspendCoroutine { continuation ->
        collection.document("total").get().addOnSuccessListener { document ->
            val totalData = document.toObject(TotalData::class.java).also {
                Log.d("knu", "succeed to get $it of $name")
            }
            continuation.resume(totalData?.total ?: 0)
        }.addOnFailureListener {
            Log.d("knu", "failed to get Total of $name\n$it")
            continuation.resume(0)
        }
    }

    suspend fun upload(mapDataList: List<MapData>): Boolean = suspendCoroutine { continuation ->
        val batch = firestore.batch()

        mapDataList.forEachIndexed { index, mapData ->
            batch.set(collection.document(index.toString()), mapData)
        }
        val totalData = TotalData(mapDataList.size)
        batch.set(collection.document("total"), totalData)

        batch.commit().addOnSuccessListener {
            Log.d("knu", "succeed to upload data into $name")
            continuation.resume(true)
        }.addOnFailureListener {
            Log.d("knu", "failed to upload data into $name\n$it")
            continuation.resume(false)
        }
    }
}