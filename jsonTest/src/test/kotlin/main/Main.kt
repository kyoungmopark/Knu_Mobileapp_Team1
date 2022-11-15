package main

import data.*

fun main(args: Array<String>) {
    for (rawData in DaeguBukguService().receive()) {
        print(rawData)
        print("\n")
    }
    for (rawData in DaeguJungguService().receive()) {
        print(rawData)
        print("\n")
    }
}