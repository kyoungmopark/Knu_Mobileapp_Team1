package com.example.server

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mapdata.MapData
import com.example.server.rawdata.DaeguBukguData
import com.example.server.rawdata.DaeguJungguData
import com.example.server.rawdata.RawDataService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var geocoder: Geocoder
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        geocoder = Geocoder(this, Locale.KOREA)
        db = FirebaseFirestore.getInstance()

        CoroutineScope(Dispatchers.IO).launch {
            uploadDaeguBukguData()
            //uploadDeaguJungguData()
        }
    }

    fun uploadDaeguBukguData()
    {
        val service = RawDataService(DaeguBukguData::class, "https://api.odcloud.kr/api/15101420/v1/uddi:37a2f399-4dd0-4483-8c3a-97c81e7171c8?perPage=0&serviceKey=N5y%2B8eZU60ZCyJtY9JpQwgjexI5cM5LAK8S4s1p3WHgtTMXy24R4z%2Bt7njRjWjXdjVwteW39U5SPpLsLcAnB%2Fg%3D%3D")
        val rawDataList = service.receiveByGroup()
        val mapDataList = mutableListOf<com.example.mapdata.MapData>()

        db.collection("bukgu").document("0").get().addOnSuccessListener { document ->
            class TempTotalCnt {
                var total: Int = 0
            }
            val temptotalcnt = document.toObject(TempTotalCnt::class.java)
            if ((document != null) && (temptotalcnt?.total == rawDataList.size)) {
                Log.d("showdisplay", "Already update")
            } else {
                Log.d("showdisplay", "Updating")
                for (rawData in rawDataList) {
                    try {
                        Log.d("UPLOAD", "${rawData.key}")
                        val address = geocoder.getFromLocationName(rawData.key, 1)[0]
                        val geoPoint = GeoPoint(address.latitude, address.longitude)
                        val equipments = listOf<String>()
                        mapDataList.add(com.example.mapdata.MapData(rawData.key, geoPoint, equipments))
                        Log.d("UPLOAD", "${rawData.key} -> $geoPoint")
                    } catch (e: Exception) {
                        val geoPoint = GeoPoint(0.0, 0.0)
                        val equipments = listOf<String>(rawData.key, "Not to find latitude and longitude")
                        mapDataList.add(com.example.mapdata.MapData(rawData.key, geoPoint, equipments))
                        Log.d("UPLOAD", e.toString())
                    }
                }

                Log.d("rawDatatList size", "size: ${rawDataList.size}")

                var cnt: Int = 0
                for(mapData in mapDataList){
                    cnt++
                    db.collection("bukgu").document(cnt.toString()).set(mapData)
                }

                class Totalcnt(var total: Int)
                var totalcnt = Totalcnt(cnt)
                db.collection("bukgu").document("0").set(totalcnt)
                Log.d("showdisplay", "Finish updating")
            }
        }
    }

    fun uploadDeaguJungguData()
    {
        val service = RawDataService(DaeguJungguData::class, "https://api.odcloud.kr/api/15101506/v1/uddi:1958c212-87f4-4042-9466-eb03cb718ff1?perPage=0&serviceKey=N5y%2B8eZU60ZCyJtY9JpQwgjexI5cM5LAK8S4s1p3WHgtTMXy24R4z%2Bt7njRjWjXdjVwteW39U5SPpLsLcAnB%2Fg%3D%3D")
    }
}