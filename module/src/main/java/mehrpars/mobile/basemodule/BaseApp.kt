package mehrpars.mobile.basemodule

import android.annotation.SuppressLint
import android.net.NetworkInfo
import android.util.Log
import androidx.databinding.ObservableField
import androidx.multidex.MultiDexApplication
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ly.count.android.sdk.Countly
import ly.count.android.sdk.CountlyConfig
import ly.count.android.sdk.DeviceId
import mehrpars.mobile.basemodule.utils.LocaleUtils
import java.util.*


abstract class BaseApp : MultiDexApplication() {
    val isConnectedToNetwork = ObservableField<Boolean>().apply { set(false) }
    val isConnectedToInternet = ObservableField<Boolean>().apply { set(false) }
    lateinit var settings: InternetObservingSettings

    companion object {
        var appLocale: Locale? = null
    }

    override fun onCreate() {
        super.onCreate()

        initAppLocale()
        initCountly()
        initNetworkCheckUrl()

        checkNetworkConnectivity()
    }

    /**
     * set default application Locale
     * */
    open fun initAppLocale() {
        appLocale = Locale("en", "US")
        LocaleUtils.setLocale(appLocale!!)
        LocaleUtils.updateConfig(this, baseContext.resources.configuration)
    }

    abstract fun getCountlyServerUrl(): String?

    abstract fun getCountlyApiKey(): String?

    /**
     * setup countly for crash reporting
     * */
    open fun initCountly() {
        if (getCountlyServerUrl().isNullOrEmpty() || getCountlyApiKey().isNullOrEmpty()) {
            Log.i("Countly", "----countly SERVER_URL or API_KEY is empty")
            return
        }

        // setup countly for crash reporting
        val config = CountlyConfig(this, getCountlyApiKey(), getCountlyServerUrl())
        config.setLoggingEnabled(BuildConfig.DEBUG)
            .enableCrashReporting()
            .setIdMode(DeviceId.Type.OPEN_UDID)
        Countly.sharedInstance().init(config)
    }

    abstract fun getNetworkCheckUrl(): String?

    open fun initNetworkCheckUrl() {
        getNetworkCheckUrl()?.let { networkCheckUrl ->
            settings = InternetObservingSettings.builder()
                .host(networkCheckUrl)
                .strategy(SocketInternetObservingStrategy())
                .build()

            checkInternetConnectivity()
        }
    }

    @SuppressLint("CheckResult")
    private fun checkNetworkConnectivity() {
        // observe network connectivity
        ReactiveNetwork
            .observeNetworkConnectivity(this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { connectivity ->
                if (connectivity.state() == NetworkInfo.State.CONNECTED) {
                    checkInternetConnectivity()
                    isConnectedToNetwork.set(true)
                    Log.i("NetworkState", "----connected(${settings.host()}): true")
                } else {
                    isConnectedToNetwork.set(false)
                    Log.i("NetworkState", "----connected(${settings.host()}): false")
                    isConnectedToInternet.set(false)
                    Log.i("InternetState", "----connected(${settings.host()}): false")
                }
            }
    }

    @SuppressLint("CheckResult")
    private fun checkInternetConnectivity() {
        val single = ReactiveNetwork.checkInternetConnectivity(settings)
        single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { hasInternetAccess ->
                isConnectedToInternet.set(hasInternetAccess)
                Log.i("InternetState", "----connected(${settings.host()}): $hasInternetAccess")
            }
    }

    fun isConnectedToNetwork(): Boolean {
        return isConnectedToNetwork.get() ?: false
    }

    fun isConnectedToInternet(): Boolean {
        val connected = isConnectedToInternet.get() ?: false
        if (!connected) checkInternetConnectivity()
        return connected
    }
}