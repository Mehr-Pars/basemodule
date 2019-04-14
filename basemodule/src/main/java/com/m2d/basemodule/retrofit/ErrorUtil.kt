package com.m2d.basemodule.retrofit

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response


object ErrorUtil {

    fun <T> parseError(response: Response<T>): Error {
        return try {
            val converter: Converter<ResponseBody, Error> = RetrofitUtil.getRetrofit()
                    .responseBodyConverter(Error::class.java, arrayOfNulls<Annotation>(0))

            if (response.errorBody() != null) {
                converter.convert(response.errorBody()!!)
            } else {
                Error(500, "خطا غیرمنتظره")
            }
        } catch (e: Exception) {
            Error(500, "مشکلی در دریافت اطلاعات بوجود آمده است")
        }
    }
}