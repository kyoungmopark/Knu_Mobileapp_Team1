package main

import rawdata.*

fun main(args: Array<String>) {
    printDaeguBukgu()
    printDaeguJunggu()
}

fun printDaeguBukgu() {
    val service = RawDataService(DaeguBukguData::class, "https://api.odcloud.kr/api/15101420/v1/uddi:37a2f399-4dd0-4483-8c3a-97c81e7171c8?perPage=0&serviceKey=N5y%2B8eZU60ZCyJtY9JpQwgjexI5cM5LAK8S4s1p3WHgtTMXy24R4z%2Bt7njRjWjXdjVwteW39U5SPpLsLcAnB%2Fg%3D%3D")

    for (rawData in service.receive()) {
        print("$rawData\n")
    }
    for (rawDataGroup in service.receiveByGroup()) {
        print("$rawDataGroup\n")
    }
}

fun printDaeguJunggu() {
    val service = RawDataService(DaeguJungguData::class, "https://api.odcloud.kr/api/15101506/v1/uddi:1958c212-87f4-4042-9466-eb03cb718ff1?perPage=0&serviceKey=N5y%2B8eZU60ZCyJtY9JpQwgjexI5cM5LAK8S4s1p3WHgtTMXy24R4z%2Bt7njRjWjXdjVwteW39U5SPpLsLcAnB%2Fg%3D%3D")

    for (rawData in service.receive()) {
        print("$rawData\n")
    }
    for (rawDataGroup in service.receiveByGroup()) {
        print("$rawDataGroup\n")
    }
}