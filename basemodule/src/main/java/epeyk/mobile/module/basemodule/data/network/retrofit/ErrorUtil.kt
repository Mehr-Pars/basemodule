package epeyk.mobile.module.basemodule.data.network.retrofit

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response


object ErrorUtil {

    fun <T> parseError(response: Response<T>): Error {
        return try {
            val converter: Converter<ResponseBody, Error> = RetrofitUtil.getRetrofit()
                    .responseBodyConverter(Error::class.java, arrayOfNulls<Annotation>(0))

            response.errorBody()?.let {
                converter.convert(it)
            } ?: run {
                Error(500, "خطا غیرمنتظره")
            }
        } catch (e: Exception) {
            Error(500, "مشکلی در دریافت اطلاعات بوجود آمده است")
        }
    }
}