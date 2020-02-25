package epeyk.mobile.basemodule

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import androidx.databinding.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

fun Disposable.disposedBy(disposable: CompositeDisposable) {
    disposable.add(this)
}

fun <T : Observable> T.addOnPropertyChanged(callback: (T) -> Unit) =
    object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable?, i: Int) =
            callback((observable as T))
    }.also { addOnPropertyChangedCallback(it) }.let {
        Disposables.fromAction { removeOnPropertyChangedCallback(it) }
    }

@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
fun isConnectingToInternet(context: Context): Boolean {
    try {
        val connectivity =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivity.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return false
}