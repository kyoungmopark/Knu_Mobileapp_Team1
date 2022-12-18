package com.example.server.rawdata

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class JsonDownloader(urlString: String) {
    private val url = URL(urlString)

    fun receive(): String {
        return try {
            val buffer = StringBuffer()
            url.openConnection().getInputStream().use { stream ->
                InputStreamReader(stream).use { streamReader ->
                    BufferedReader(streamReader).use { bufferReader ->
                        while (true) buffer.append(bufferReader.readLine() ?: break)
                    }
                }
            }
            Log.d("dev", "succeed to download")
            buffer.toString()
        } catch (e: Exception) {
            Log.d("dev", "failed to download")
            ""
        }
    }
}