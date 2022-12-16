package com.example.server.rawdata

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class JsonConn(urlString: String) {
    private val url = URL(urlString)

    fun receive(): String {
        val buffer = StringBuffer()
        url.openConnection().getInputStream().use { stream ->
            InputStreamReader(stream).use { streamReader ->
                BufferedReader(streamReader).use { bufferReader ->
                    while (true) buffer.append(bufferReader.readLine() ?: break)
                }
            }
        }
        return buffer.toString()
    }
}