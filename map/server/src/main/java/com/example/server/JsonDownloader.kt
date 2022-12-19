package com.example.server

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

// 공공데이터 API로부터 JSON을 다운로드받는다
class JsonDownloader(private val name: String, urlString: String) {
    private val url = URL(urlString)

    fun download(): String {
        return try {
            val buffer = StringBuffer()
            url.openConnection().getInputStream().use { stream ->
                InputStreamReader(stream).use { streamReader ->
                    BufferedReader(streamReader).use { bufferReader ->
                        while (true) buffer.append(bufferReader.readLine() ?: break)
                    }
                }
            }
            buffer.toString().also {
                Log.d("knu", "succeed to download json from $name")
            }
        } catch (e: Exception) {
            "".also {
                Log.d("knu", "failed to download json from $name")
            }
        }
    }
}