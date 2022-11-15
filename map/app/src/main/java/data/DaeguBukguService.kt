package data

class DaeguBukguService: RawDataService(
    "https://api.odcloud.kr/api/15101420/v1/uddi:37a2f399-4dd0-4483-8c3a-97c81e7171c8?perPage=0&serviceKey=N5y%2B8eZU60ZCyJtY9JpQwgjexI5cM5LAK8S4s1p3WHgtTMXy24R4z%2Bt7njRjWjXdjVwteW39U5SPpLsLcAnB%2Fg%3D%3D"
) {
    override fun receive() =
        conn.receive().deserialize<DaeguBukguData>().groupBy { it.getKey() }
}