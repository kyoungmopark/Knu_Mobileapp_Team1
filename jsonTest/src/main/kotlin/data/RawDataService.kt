package data

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

abstract class RawDataService(urlString: String) {
    protected val gson = Gson()
    protected val conn = JsonConn(urlString)

    abstract fun receive(): Map<String, List<RawData>>

    protected inline fun <reified T : RawData> String.deserialize(): List<T> =
        gson.fromJson(
            JsonParser.parseString(this).asJsonObject.get("data"),
            object: TypeToken<List<T>>() {}.type
        )
}