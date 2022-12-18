package com.example.server

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.server.mapdata.*
import com.example.server.rawdata.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var dataConverter: DataConverter

    private lateinit var daeguBukguDataService: DataService<DaeguBukguData>
    private lateinit var daeguJungguDataService: DataService<DaeguJungguData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataConverter = DataConverter(this)

        daeguBukguDataService = DataService(
            RawDataService(DaeguBukguData::class, getString(R.string.daegu_bukgu_url_base), getString(R.string.service_key)),
            dataConverter,
            MapDataService(getString(R.string.daegu_bukgu_collection_name))
        )
        daeguJungguDataService = DataService(
            RawDataService(DaeguJungguData::class, getString(R.string.daegu_junggu_url_base), getString(R.string.service_key)),
            dataConverter,
            MapDataService(getString(R.string.daegu_junggu_collection_name))
        )

        CoroutineScope(Dispatchers.IO).launch {
            daeguBukguDataService.update()
            daeguJungguDataService.update()
        }
    }

    /*fun uploadDaeguBukguData()
    {
        val geocoder = Geocoder(this, Locale.KOREA)
        val db = FirebaseFirestore.getInstance()

        val service = RawDataService(DaeguBukguData::class, DaeguBukguData.urlBase,
            "N5y%2B8eZU60ZCyJtY9JpQwgjexI5cM5LAK8S4s1p3WHgtTMXy24R4z%2Bt7njRjWjXdjVwteW39U5SPpLsLcAnB%2Fg%3D%3D")
        val rawDataList = service.receiveByGroup()
        val mapDataList = mutableListOf<MapData>()

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
                        val mapData = MapData().apply {
                            geoPoint = GeoPoint(address.latitude, address.longitude)
                            equipments = listOf(rawData.value[0].getEquipmentsToList())
                            completeAddress = rawData.key
                        }
                        mapDataList.add(mapData)
                        Log.d("UPLOAD", "${rawData.key} -> ${mapData.geoPoint}")
                    } catch (e: Exception) {
                        val mapData = MapData().apply {
                            equipments = listOf(rawData.value[0].getEquipmentsToList())
                            completeAddress = rawData.key
                        }
                        mapDataList.add(mapData)
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
    }*/
}