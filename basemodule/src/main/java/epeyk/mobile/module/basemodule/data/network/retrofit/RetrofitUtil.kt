package epeyk.mobile.module.basemodule.data.network.retrofit

import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
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
        Log.d("bootiyar:baseModule", "RetrofitUtil:init")
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            Log.d("bootiyar:baseModule", "RetrofitUtil:KITKAT(19) or JELLY_BEAN(16)")
            var spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA
                )
                .build()
            httpClient.connectionSpecs(Collections.singletonList(spec))
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