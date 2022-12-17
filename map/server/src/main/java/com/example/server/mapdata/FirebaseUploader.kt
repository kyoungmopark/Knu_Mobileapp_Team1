package com.example.server.mapdata

import com.example.mapdata.MapData
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUploader {
    private class Total { var total = 0 }

    private val firestore = FirebaseFirestore.getInstance()

    fun isRequired(collectionPath: String, dataSize: Int): Boolean {
        val collectionReference = firestore.collection(collectionPath)
        var total: Total? = null

        collectionReference.document("total").get().addOnSuccessListener { documentSnapshot ->
            total = documentSnapshot.toObject(Total::class.java)
        }

        return total?.total != dataSize
    }

    fun update(collectionPath: String, mapDataList: List<MapData>) {
        val collectionReference = firestore.collection(collectionPath)

        mapDataList.forEachIndexed { index, mapData ->
            collectionReference.document(index.toString()).set(mapData)
        }

        collectionReference.document("total").set(Total().apply {
            total = mapDataList.size
        })
    }
}