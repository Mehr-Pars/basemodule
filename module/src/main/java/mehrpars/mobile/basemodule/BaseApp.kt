package mehrpars.mobile.basemodule

import android.annotation.SuppressLint
import android.app.Activity
import android.net.NetworkInfo
import android.util.Log
import androidx.databinding.ObservableField
import androidx.multidex.MultiDexApplication
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mehrpars.mobile.basemodule.utils.LocaleUtils
import mehrpars.mobile.debugtools.ui.activity.error_activity.CustomActivityOnCrash
import java.util.*


abstract class BaseApp : MultiDexApplication() {
    val isConnectedToNetwork = ObservableField<Boolean>()
    val isConnectedToInternet = ObservableField<Boolean>()
    lateinit var settings: InternetObservingSettings

    companion object {
        var appLocale: Locale? = null
    }

    /**
     * base url without prefix for checking network connectivity (ping url)
     * */
    abstract fun getNetworkCheckUrl(): String?

    /**
     * returns restart activity class (ie, MainActivity in your project) to be called after app crash
     * */
    abstract fun getRestartActivity(): Class<out Activity>?

    override fun onCreate() {
        super.onCreate()

        initAppLocale()

        installCrashHandler()

        initNetworkCheckUrl()

//        checkNetworkConnectivity()
    }

    /**
     * initialize crash handler
     * */
    open fun installCrashHandler() {
        getRestartActivity()?.let { activity ->
            CustomActivityOnCrash.restartActivityClass = activity
            CustomActivityOnCrash.install(this)
        }
    }

    /**
     * set default application Locale
     * */
    open fun initAppLocale() {
        appLocale = Locale("en", "US")
        LocaleUtils.setLocale(appLocale!!)
        LocaleUtils.updateConfig(this, baseContext.resources.configuration)
    }

    open fun initNetworkCheckUrl() {
        isConnectedToNetwork.set(false)
        isConnectedToInternet.set(false)
        getNetworkCheckUrl()?.let { networkCheckUrl ->
            settings = InternetObservingSettings.builder()
                .host(networkCheckUrl)
                .strategy(SocketInternetObservingStrategy())
                .build()

            checkNetworkConnectivity()
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
        return isConnectedToNetwork.get() == true
    }

    fun isConnectedToInternet(): Boolean {
        val connected = isConnectedToInternet.get() == true
        if (!connected) checkInternetConnectivity()
        return connected
    }
}