package mehrpars.mobile.basemodule.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * this class is created in order to convert StringLists into appropriate string type for saving
 * data in room database
 * */
class DataConverter {
    @TypeConverter
    fun fromStringList(stringList: List<String?>?): String? {
        if (stringList == null) return null
        val type = object : TypeToken<List<String?>?>() {}.type
        return Gson().toJson(stringList, type)
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        if (data == null) return null
        val type = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(data, type)
    }
}