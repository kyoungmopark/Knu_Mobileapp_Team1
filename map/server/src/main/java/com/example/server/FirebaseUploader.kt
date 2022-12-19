package com.example.server

import android.util.Log
import com.example.mapdata.MapData
import com.example.server.data.TotalData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// 데이터를 파이어베이스에 업로드한다.
class FirebaseUploader(name: String) {
    companion object {
        private val firestore by lazy { FirebaseFirestore.getInstance() }
    }

    private val collection = firestore.collection(name)
    private lateinit var onCompleted: () -> Unit

    suspend fun getTotal(): Int {
        return suspendCoroutine { continuation ->
            collection.document("total").get().addOnSuccessListener { snapshot ->
                val totalData = snapshot.toObject(TotalData::class.java).also {
                    Log.d("dev", "succeed to get $it")
                }
                val total = totalData?.total ?: 0
                continuation.resume(total)
            }.addOnFailureListener {
                continuation.resume(0).also {
                    Log.d("dev", "failed to get Total")
                }
            }
        }
    }

    fun upload(mapDataList: List<MapData>) {
        val batch = firestore.batch()

        mapDataList.forEachIndexed { index, mapData ->
            batch.set(collection.document(index.toString()), mapData)
        }

        val totalData = TotalData(mapDataList.size)
        batch.set(collection.document("total"), totalData)

        batch.commit().addOnCompleteListener {
            onCompleted()
        }.addOnSuccessListener {
            Log.d("dev", "succeed to upload")
        }.addOnFailureListener {
            Log.d("dev", "failed to upload")
        }
    }

    fun setOnCompletedListener(callback: () -> Unit) {
        onCompleted = callback
    }
}