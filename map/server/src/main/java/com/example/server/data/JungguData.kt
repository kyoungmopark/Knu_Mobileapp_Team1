package com.example.server.data

import com.google.gson.annotations.SerializedName

data class JungguData(

    @SerializedName("관리번호")
    val serialNumber: String,

    @SerializedName("기구명")
    val equipment: String,

    @SerializedName("담당부서")
    val department: String,

    @SerializedName("시군구명")
    val district: String,

    @SerializedName("시도명")
    val city: String,

    @SerializedName("위치")
    val location: String,

    @SerializedName("유형")
    val locationType: String,

    @SerializedName("주소")
    val address: String

): DeserializedData() {
    override fun getCompleteAddress() = "$city $district $address"
    override fun getEquipmentsToList() = equipment
}