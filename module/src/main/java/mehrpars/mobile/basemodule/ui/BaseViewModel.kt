package mehrpars.mobile.basemodule.ui

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.*
import mehrpars.mobile.basemodule.BaseApp
import java.util.*


abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {
    protected val compositeDisposable = CompositeDisposable()
    protected val context by lazy { application }
    protected var arguments: Bundle? = null
    protected var passedIntent: Intent? = null
    private val networkCheckDelay: Long = 1500
    val error = MutableLiveData<Error>()
    private val requestQueue = LinkedList<SimpleRequest>()
    private val application: BaseApp by lazy {
        if (app !is BaseApp)
            throw Exception("application must be of type BaseApp, open manifest and set application -> name to BaseApp or just extend your application from BaseApp")
        else
            app
    }

    init {
        this.initAdapter()
    }

    /**
     * initialize your adapter(s) here
     */
    protected open fun initAdapter() {
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
            if (!application.isConnectedToInternet()) // wait for network check
                delay(networkCheckDelay)

            if (application.isConnectedToInternet()) {
                withContext(Dispatchers.Main) { simpleRequest.onExecute() }
            } else if (retry) {
                requestQueue.add(simpleRequest)
                error.postValue(Error(ErrorType.CONNECTION_ERROR))
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

    enum class ErrorType {
        DEFAULT, NETWORK_ERROR, CONNECTION_ERROR
    }

    data class Error(
        val type: ErrorType = ErrorType.DEFAULT,
        var errorMessage: String? = "",
        var rawError: Throwable? = null
    )
}