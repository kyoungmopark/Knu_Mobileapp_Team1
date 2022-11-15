package main

import data.*

fun main(args: Array<String>) {
    val daeguBukguService = DaeguBukguService()
    val daeguJungguService = DaeguJungguService()

    val daeguBukguDataList = daeguBukguService.receive()

    for (rawData in daeguBukguDataList) {
        print(rawData)
        print("\n")
    }
    for (rawData in daeguJungguService.receive()) {
        print(rawData)
        print("\n")
    }
}