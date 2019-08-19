package epeyk.mobile.module.basemodule.data.network.retrofit

import com.google.gson.annotations.SerializedName

data class Error(
        @SerializedName("statusCode") val statusCode: Int,
        @SerializedName("message") var message: String
)