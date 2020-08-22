package mehrpars.mobile.basemodule

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.databinding.Observable
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.lang.reflect.Method

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