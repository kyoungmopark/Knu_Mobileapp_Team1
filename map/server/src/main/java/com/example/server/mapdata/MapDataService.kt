package com.example.server.mapdata

import android.util.Log
import com.example.mapdata.MapData
import com.google.firebase.firestore.FirebaseFirestore

class MapDataService(name: String) {
    private val collection = firestore.collection(name)

    fun isRequiredUpdate(dataSize: Int): Boolean {
        var total: Total? = null

        collection.document("total").get().addOnSuccessListener { document ->
            total = document.toObject(Total::class.java)
        }

        if (total?.total != dataSize) {
            Log.d("upload", "required")
            return true
        } else {
            Log.d("upload", "pass")
            return false
        }
    }

    fun send(mapDataList: List<MapData>) {

        mapDataList.forEachIndexed { index, mapData ->
            collection.document(index.toString()).set(mapData)
            Log.d("upload", "${mapData.completeAddress}, ${mapData.geoPoint}")
        }

        val total = Total().apply { total = mapDataList.size }
        collection.document("total").set(total)
        Log.d("upload", "completed (total is ${total.total})")
    }

    companion object {
        private class Total { var total = 0 }

        private val firestore by lazy {
            FirebaseFirestore.getInstance()
        }
    }
}