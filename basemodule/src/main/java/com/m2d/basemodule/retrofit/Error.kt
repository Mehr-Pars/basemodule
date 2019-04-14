package com.m2d.basemodule.retrofit

import com.google.gson.annotations.SerializedName

data class Error(
        @SerializedName("statusCode") val statusCode: Int,
        @SerializedName("message") var message: String
)