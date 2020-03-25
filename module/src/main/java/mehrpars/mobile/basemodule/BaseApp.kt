package mehrpars.mobile.basemodule

import android.annotation.SuppressLint
import android.net.NetworkInfo
import android.util.Log
import androidx.databinding.ObservableField
import androidx.multidex.MultiDexApplication
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import mehrpars.mobile.basemodule.utils.LocaleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*


open class BaseApp : MultiDexApplication() {
    val isConnectedToNetwork = ObservableField<Boolean>().apply { set(false) }
    val isConnectedToInternet = ObservableField<Boolean>().apply { set(false) }

    companion object {
        var appLocale: Locale? = null
        var settings: InternetObservingSettings = InternetObservingSettings.builder()
            .host("www.google.com")
            .strategy(SocketInternetObservingStrategy())
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        appLocale?.let { locale ->
            LocaleUtils.setLocale(locale)
            LocaleUtils.updateConfig(this, baseContext.resources.configuration)
        }

        checkNetworkConnectivity()
    }

    fun initNetworkCheckUrl(networkCheckUrl: String) {
        settings = InternetObservingSettings.builder()
            .host(networkCheckUrl)
            .strategy(SocketInternetObservingStrategy())
            .build()

        checkInternetConnectivity()
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