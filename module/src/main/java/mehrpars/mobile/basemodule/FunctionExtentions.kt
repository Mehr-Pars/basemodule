package mehrpars.mobile.basemodule

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import mehrpars.mobile.basemodule.utils.Event
import java.lang.reflect.Method
import java.net.UnknownHostException


fun Context.isDebuggable(): Boolean {
    var debuggable = false
    val pm = packageManager
    try {
        val appInfo = pm.getApplicationInfo(packageName, 0)
        debuggable = 0 != appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    } catch (e: PackageManager.NameNotFoundException) {
        /*debuggable variable will remain false*/
    }
    return debuggable
}

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

inline fun <reified Args : NavArgs> Bundle.getSafeArguments(): Args {
    val method: Method = Args::class.java.getMethod("fromBundle", Bundle::class.java)

    return method.invoke(null, this) as Args
}

fun NavController.safeNavigate(currentDestinationId: Int, directions: NavDirections) {
    if (currentDestination?.id == currentDestinationId)
        navigate(directions)
}

fun Throwable?.isNetworkError(): Boolean = this is UnknownHostException

fun <T> MutableLiveData<Event<T>>.postEvent(value: T) {
    postValue(Event(value))
}