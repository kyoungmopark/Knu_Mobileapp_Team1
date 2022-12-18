package com.example.server.mapdata

import android.util.Log
import com.example.mapdata.MapData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MapDataService(name: String) {
    private val collection = firestore.collection(name)

    suspend fun isRequiredUpdate(dataSize: Int): Boolean {

        Log.d("upload", "data size = $dataSize")

        val total = suspendCoroutine { continuation ->
            collection.document("total").get().addOnSuccessListener {
                continuation.resume(it.toObject(Total::class.java).apply {
                    Log.d("upload", "succeed to get $this")
                })
            }.addOnFailureListener {
                Log.d("upload", "failed to get total")
                continuation.resume(null)
            }
        }

        return if (total?.total != dataSize) {
            Log.d("upload", "required")
            true
        } else {
            Log.d("upload", "pass")
            false
        }
    }

    fun send(mapDataList: List<MapData>) {

        mapDataList.forEachIndexed { index, mapData ->
            collection.document(index.toString()).set(mapData).addOnSuccessListener {
                Log.d("upload", "succeed to send $mapData")
            }.addOnFailureListener {
                Log.d("upload", "failed to send $mapData")
            }
        }

        val total = Total().apply { total = mapDataList.size }

        collection.document("total").set(total).addOnSuccessListener {
            Log.d("upload", "succeed to send $total")
        }.addOnFailureListener {
            Log.d("upload", "failed to send $total")
        }
    }

    companion object {
        private class Total { var total = 0 }

        private val firestore by lazy {
            FirebaseFirestore.getInstance()
        }
    }
}