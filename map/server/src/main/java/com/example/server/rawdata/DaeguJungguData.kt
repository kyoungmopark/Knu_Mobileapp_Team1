package com.example.server.rawdata

import com.google.gson.annotations.SerializedName

data class DaeguJungguData(

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

): RawData() {
    override fun getCompleteAddress() = "$city $district $address"
    override fun getEquipmentsToList() = equipment

    companion object {
        val urlBase = "https://api.odcloud.kr/api/15101506/v1/uddi:1958c212-87f4-4042-9466-eb03cb718ff1"
    }
}