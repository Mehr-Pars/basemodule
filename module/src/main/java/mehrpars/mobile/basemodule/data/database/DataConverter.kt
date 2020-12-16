package mehrpars.mobile.basemodule.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Ali Arasteh
 */

/**
 * this class is created in order to convert StringLists into appropriate string type for saving
 * data in room database
 * */
class DataConverter {
    /**
     * Converts List of String into String object for saving in database
     *
     * @param stringList the list to be converted into String
     */
    @TypeConverter
    fun fromStringList(stringList: List<String?>?): String? {
        if (stringList == null) return null
        val type = object : TypeToken<List<String?>?>() {}.type
        return Gson().toJson(stringList, type)
    }

    /**
     * Converts String object fetched from database into List of String
     *
     * @param data String object to be converted into List of String
     */
    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        if (data == null) return null
        val type = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(data, type)
    }
}