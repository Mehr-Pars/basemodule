package mehrpars.mobile.basemodule.ui

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.*
import mehrpars.mobile.basemodule.BaseApp
import mehrpars.mobile.basemodule.data.error.GeneralError
import mehrpars.mobile.basemodule.data.error.NetworkError
import mehrpars.mobile.basemodule.utils.SingleLiveEvent
import java.util.*


abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {
    protected val compositeDisposable = CompositeDisposable()
    protected val context by lazy { application }
    protected var arguments: Bundle? = null
    protected var passedIntent: Intent? = null
    private val networkCheckDelay: Long = 1500
    private val requestQueue = LinkedList<SimpleRequest>()
    private val _generalError = SingleLiveEvent<List<GeneralError>>()
    val generalError: LiveData<List<GeneralError>> by lazy { _generalError }
    private val application: BaseApp by lazy {
        if (app !is BaseApp) {
            throw Exception("application must be of type BaseApp, open manifest and set application -> name to BaseApp or just extend your application from BaseApp")
        } else {
        }
        app
    }

    /**
     * handle passed arguments here
     */
    open fun handleArguments(arguments: Bundle) {
        this.arguments = arguments
    }

    /**
     * handle passed intent here
     */
    open fun handleIntent(intent: Intent) {
        passedIntent = intent
    }

    protected fun safeRequest(
        retry: Boolean = true,
        onCancelAction: (() -> Unit)? = null,
        onExecuteAction: () -> Unit
    ) {
        safeRequest(object : SimpleRequest() {
            override fun onExecute() {
                onExecuteAction()
            }

            override fun onCancel() {
                onCancelAction?.invoke()
            }
        }, retry)
    }

    protected fun safeRequest(simpleRequest: SimpleRequest, retry: Boolean = true) {
        GlobalScope.launch {
            if (!application.isConnectedToInternet()) {
            } // wait for network check
            delay(networkCheckDelay)

            if (application.isConnectedToInternet()) {
                withContext(Dispatchers.Main) { simpleRequest.onExecute() }
            } else if (retry) {
                requestQueue.add(simpleRequest)
                setError(NetworkError.instance())
            } else {
                Log.e("SafeRequest", "Request canceled due to internet connection error!")
            }
        }
    }

    fun retryOnRequestQueue() {
        for (i in requestQueue.indices) {
            val request = requestQueue.remove()
            safeRequest(request)
        }
    }

    fun cancelRequestQueue() {
        for (i in requestQueue.indices) {
            requestQueue.remove().onCancel()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelRequestQueue()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    abstract class SimpleRequest {
        abstract fun onExecute()

        open fun onCancel() {}
    }

    // region Error Handling Helper Methods
    protected fun setError(error: GeneralError) {
        _generalError.postValue(listOf(error))
    }

    protected fun setErrors(errorList: List<GeneralError>?) {
        _generalError.postValue(errorList)
    }
    // endregion
}