package data

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

abstract class DataService(urlString: String) {
    protected val gson = Gson()
    protected val conn = JsonConn(urlString)

    abstract fun receive(): List<MarkerData>

    protected inline fun <reified T> String.deserialize() : List<T> where T: RawData =
        gson.fromJson(
            JsonParser.parseString(this).asJsonObject.get("data"),
            object: TypeToken<List<T>>() {}.type
        )
    protected inline fun <reified T> List<T>.preprocess() : List<MarkerData> where T: RawData =
        this.groupBy { it.getCompleteAddress() }.map {
            MarkerData(0.0, 0.0)
        }
}