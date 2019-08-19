package epeyk.mobile.module.basemodule.data.model

import com.google.gson.Gson

open class BaseModel : ErrorResponse() {
    fun toJsString(): String = Gson().toJson(this).toString()

    companion object {
        open fun toObject(str: String): BaseModel = Gson().fromJson(str, BaseModel::class.java)
    }
}