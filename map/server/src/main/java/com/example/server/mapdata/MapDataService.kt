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

        val total = suspendCoroutine { continuation ->
            collection.document("total").get().addOnSuccessListener {
                continuation.resume(
                    it.toObject(Total::class.java).also {
                        Log.d("dev", "succeed to check $it")
                    }
                )
            }.addOnFailureListener {
                Log.d("dev", "failed to check total")
                continuation.resume(null)
            }
        }

        return if (total?.total != dataSize) {
            Log.d("dev", "required update")
            true
        } else {
            Log.d("dev", "passed update")
            false
        }
    }

    fun update(mapDataList: List<MapData>) {

        mapDataList.forEachIndexed { index, mapData ->
            collection.document(index.toString()).set(mapData).addOnSuccessListener {
                Log.d("dev", "succeed to update $mapData")
            }.addOnFailureListener {
                Log.d("dev", "failed to update $mapData")
            }
        }

        val total = Total().apply { total = mapDataList.size }

        collection.document("total").set(total).addOnSuccessListener {
            Log.d("dev", "succeed to update $total")
        }.addOnFailureListener {
            Log.d("dev", "failed to update $total")
        }
    }

    companion object {
        private class Total { var total = 0 }

        private val firestore by lazy {
            FirebaseFirestore.getInstance()
        }
    }
}