package com.example.server.rawdata

import com.google.gson.annotations.SerializedName

data class DaeguBukguData(

    @SerializedName("관리부서")
    val department: String,

    @SerializedName("관리부서연락처")
    val contact: String,

    @SerializedName("데이터기준일자")
    val baseDate: String,

    @SerializedName("설치장소")
    val location: String,

    @SerializedName("소재지 지번주소")
    val address: String,

    @SerializedName("수량")
    val quantity: Int,

    @SerializedName("시군구명")
    val district: String,

    @SerializedName("시도명")
    val city: String,

    @SerializedName("연번")
    val serialNumber: Int,

    @SerializedName("운동기구종류")
    val equipments: String

): RawData() {
    override fun getCompleteAddress() = address
    override fun getEquipmentsToList() = listOf(equipments)

    companion object {
        val urlBase = "https://api.odcloud.kr/api/15101420/v1/uddi:37a2f399-4dd0-4483-8c3a-97c81e7171c8"
    }
}