package mehrpars.mobile.basemodule.data.network

import android.content.Context
import android.os.Build
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import mehrpars.mobile.basemodule.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Ali Arasteh
 */

abstract class RetrofitConfig {
    protected val builder: Retrofit.Builder by lazy {
        getRetrofitBuilder()
    }
    protected val httpClient: OkHttpClient.Builder by lazy {
        getHttpClientBuilder()
    }

    abstract fun getBaseUrl(): String

    /**
     * initialize Retrofit
     * */
    fun init(context: Context) {
        installLowerApiCertificates(context)

        // add logger interceptor
        httpClient.addInterceptor(getLogger())

        // add main interceptor
        httpClient.addInterceptor(getMainInterceptor())
    }

    /**
     * initialize OkHttpClient instance and setup configurations like connection timeout etc.
     * */
    open fun getRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addCallAdapterFactory(getCallAdapterFactory())
            .addConverterFactory(getConverterFactory())
            .client(httpClient.build())
    }

    /**
     * initialize OkHttpClient instance and setup configurations like connection timeout etc.
     * */
    open fun getHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .readTimeout(10000, TimeUnit.SECONDS)
    }

    /**
     * initialize main interceptor
     * */
    open fun getMainInterceptor(): Interceptor {
        return Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            // add required headers
            getHeaders()?.forEach { header ->
                requestBuilder.addHeader(header.first, header.second)
            }

            val request: Request = requestBuilder.build()

            val response = chain.proceed(request)

            onResponse(response)

            response
        }
    }

    /**
     * initialize logger interceptor
     * */
    open fun getLogger(): Interceptor {
        val debuggable = BuildConfig.DEBUG
        return if (debuggable) HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        } else HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }
    }

    /**
     * initialize call adapter factory
     * */
    open fun getCallAdapterFactory(): CallAdapter.Factory {
        return RxJava3CallAdapterFactory.create()
    }

    /**
     * initialize converter factory
     * */
    open fun getConverterFactory(): Converter.Factory {
        val gson: Gson = GsonBuilder().setLenient().create()
        return GsonConverterFactory.create(gson)
    }

    /**
     * override onResponse() to do actions before upper layer observers be triggered.
     * (ie, check if response.code() == 401 then Authentication required)
     * */
    open fun onResponse(response: Response) {
    }

    /**
     * override this method and initialize your headers if necessary
     * */
    open fun getHeaders(): List<Pair<String, String>>? {
        return null
    }

    /**
     * this method handles some issues on lower api levels
     * */
    private fun installLowerApiCertificates(context: Context) {
        // install required certificates for android Api lower than 21
        if (Build.VERSION.SDK_INT in 16..21) {
            try {
                ProviderInstaller.installIfNeeded(context)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
        }

        // avoid tls error on some android versions
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            val spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA
                )
                .build()
            httpClient.connectionSpecs(
                listOf(
                    ConnectionSpec.MODERN_TLS,
                    ConnectionSpec.CLEARTEXT,
                    spec
                )
            )
        }
    }

    /**
     * create Service for a retrofit interface
     * */
    fun <T> createService(serviceClass: Class<T>): T {
        val retrofit = builder.build()
        return retrofit.create(serviceClass)
    }

}