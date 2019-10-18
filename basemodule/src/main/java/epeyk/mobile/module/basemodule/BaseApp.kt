package epeyk.mobile.module.basemodule

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


open class BaseApp : MultiDexApplication() {
    val isConnectedToNetwork = ObservableField<Boolean>().apply { set(false) }
    val isConnectedToInternet = ObservableField<Boolean>().apply { set(false) }
    var settings = InternetObservingSettings.builder()
        .host("www.google.com")
        .strategy(SocketInternetObservingStrategy())
        .build()

    override fun onCreate() {
        super.onCreate()

        checkNetworkConnectivity()
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
                    Log.i("NetworkState", "----connected: true")
                } else {
                    isConnectedToNetwork.set(false)
                    Log.i("NetworkState", "----connected: false")
                    isConnectedToInternet.set(false)
                    Log.i("InternetState", "----connected: false")
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
                Log.i("InternetState", "----connected: $hasInternetAccess")
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