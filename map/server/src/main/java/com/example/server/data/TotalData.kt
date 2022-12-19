package com.example.server.data

class TotalData() {
    var total = 0

    constructor(total: Int) : this() {
        this.total = total
    }

    override fun toString() = "Total($total)"
}