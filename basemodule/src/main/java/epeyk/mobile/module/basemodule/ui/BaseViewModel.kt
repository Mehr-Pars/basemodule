package epeyk.mobile.module.basemodule.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import epeyk.mobile.module.basemodule.BaseApp
import epeyk.mobile.module.basemodule.data.network.retrofit.ErrorHttp
import epeyk.mobile.module.basemodule.data.network.retrofit.ErrorType
import epeyk.mobile.module.basemodule.data.network.retrofit.ErrorUtil
import epeyk.mobile.module.basemodule.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import retrofit2.Response
import java.util.*


abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    protected val compositeDisposable = CompositeDisposable()
    protected val context by lazy { application }
    val error = MutableLiveData<Pair<ErrorType, ErrorHttp?>>()

    val networkError = SingleLiveEvent<Boolean>()
    private val requestQueue = LinkedList<() -> Unit>()
    private val application: BaseApp by lazy {
        if (app !is BaseApp) throw Exception("application must be of type BaseApp, open manifest and set application -> name to BaseApp or just extend your application from BaseApp")
        app as BaseApp
    }

    init {
        initViews()
        initAdapter()
    }

    /**
     * initialize your views in here
     */
    protected abstract fun initViews()

    /**
     * initialize your adapter(s) here
     */
    protected abstract fun initAdapter()

    fun handleError(e: Throwable) {
        Log.v("masood", "error : " + e.message)
        if (e is HttpException && e.response() != null) {
            error.value = Pair(ErrorType.HTTP_ERROR, errorHandle(e.response()!!))
        } else {
            error.value = Pair(ErrorType.OTHER_ERROR, null)
        }
    }

    fun <T> errorHandle(response: Response<T>): ErrorHttp {
        val error = try {
            if (response.errorBody() != null) {
                val e = ErrorUtil.parseError(response)
                when (e.message) {
                    "player not found" -> {
                        e.message = "اطلاعات یافت نشد"
                    }
                }
                ErrorHttp(e.statusCode, e.message)
            } else {
                ErrorHttp(500, "خطای غیر منتظره!")
            }
        } catch (e: Exception) {
            ErrorHttp(500, "مشکلی در ارتباط با سرور وجود دارد")
        }
        // Timber.d(error.message)
        return error
    }

    protected fun safeRequest(request: () -> Unit) {
        if (application.isConnectedToInternet()) {
            request()
        } else {
            requestQueue.add(request)
            networkError.postValue(true)
        }
    }

    fun retryOnRequestQueue() {
        for (i in requestQueue.indices) {
            val request = requestQueue.remove()
            safeRequest(request)
        }
    }

    fun cancelRequestQueue() {
        requestQueue.clear()
    }

    override fun onCleared() {
        super.onCleared()
        cancelRequestQueue()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

}