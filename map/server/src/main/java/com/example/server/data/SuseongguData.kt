package com.example.server.data

import com.google.gson.annotations.SerializedName

data class SuseongguData(

    @SerializedName("관리부서")
    val department: String,

    @SerializedName("관리부서연락처")
    val contact: String,

    @SerializedName("데이터기준")
    val baseDate: String,

    @SerializedName("설치년도")
    val year: Int,

    @SerializedName("설치장소")
    val location: String,

    @SerializedName("수량")
    val quantity: Int,

    @SerializedName("시군구명")
    val district: String,

    @SerializedName("시도명")
    val city: String,

    @SerializedName("운동기구명")
    val equipment: String,

    @SerializedName("주소")
    val address: String,

    @SerializedName("행정동명")
    val neighborhood: String
): DeserializedData() {
    override fun getCompleteAddress() = "$city $district $address"
    override fun getEquipmentsToList() = equipment
}