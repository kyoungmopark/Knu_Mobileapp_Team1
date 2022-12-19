package com.example.mapdata

class TotalData() {
    var total = 0

    constructor(total: Int) : this() {
        this.total = total
    }

    override fun toString() = "Total($total)"
}