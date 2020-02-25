package epeyk.mobile.basemodule.data.model

import com.google.gson.Gson

open class BaseResponse : ErrorResponse() {
    fun toJsString(): String = Gson().toJson(this).toString()

    companion object {
        open fun toObject(str: String): BaseResponse = Gson().fromJson(str, BaseResponse::class.java)
    }
}