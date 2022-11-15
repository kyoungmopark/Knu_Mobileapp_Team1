package data

class DaeguJungguService: RawDataService(
    "https://api.odcloud.kr/api/15101506/v1/uddi:1958c212-87f4-4042-9466-eb03cb718ff1?perPage=0&serviceKey=N5y%2B8eZU60ZCyJtY9JpQwgjexI5cM5LAK8S4s1p3WHgtTMXy24R4z%2Bt7njRjWjXdjVwteW39U5SPpLsLcAnB%2Fg%3D%3D",
) {
    override fun receive() =
        conn.receive().deserialize<DaeguJungguData>().groupBy { it.getKey() }
}