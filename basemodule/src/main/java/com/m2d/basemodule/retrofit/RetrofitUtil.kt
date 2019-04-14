package com.m2d.basemodule.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Version class: 1.1
 */

object RetrofitUtil {

    private var gson: Gson = GsonBuilder().setLenient().create()

    private val gsonFactory = GsonConverterFactory.create(gson)
    private val httpClient = OkHttpClient.Builder().retryOnConnectionFailure(false)
        .writeTimeout(10000, TimeUnit.SECONDS).connectTimeout(10000, TimeUnit.SECONDS)
        .readTimeout(10000, TimeUnit.SECONDS)

    private val logging by lazy { HttpLoggingInterceptor() }

    private lateinit var builder: Retrofit.Builder
    private lateinit var retrofit: Retrofit

    fun getRetrofit() = retrofit

    fun init(baseUrl: String, debug: Boolean) {
        if (debug) {
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
                .addNetworkInterceptor {
                    var request = it.request().newBuilder().addHeader("Connection", "close").build()
                    return@addNetworkInterceptor it.proceed(request)
                }
        }

        builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonFactory)
            .client(httpClient.build())
    }

    fun changeApiBaseUrl(newBaseUrl: String) {
        builder = Retrofit.Builder()
            .baseUrl(newBaseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonFactory)
            .client(httpClient.build())
    }

    fun <T> createService(serviceClass: Class<T>): T {
        retrofit = builder.build()
        return retrofit.create(serviceClass)
    }
}