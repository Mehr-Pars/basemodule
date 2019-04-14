package epeyk.mobile.module.basemodule.retrofit

import com.google.gson.annotations.SerializedName

data class ErrorHttp(
        @SerializedName("statusCode") val statusCode: Int,
        @SerializedName("message") val message: String
)