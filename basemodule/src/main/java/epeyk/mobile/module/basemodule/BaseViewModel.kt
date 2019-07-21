package epeyk.mobile.module.basemodule

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import epeyk.mobile.module.basemodule.retrofit.ErrorHttp
import epeyk.mobile.module.basemodule.retrofit.ErrorType
import epeyk.mobile.module.basemodule.retrofit.ErrorUtil
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import retrofit2.Response


abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val compositeDisposable = CompositeDisposable()
    protected val context by lazy { application }
    val error = MutableLiveData<Pair<ErrorType, ErrorHttp?>>()

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

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    fun handleError(e: Throwable) {
        Log.v("masood", "error : " + e.message)
        if (e is HttpException) {
            error.value = Pair(ErrorType.HTTP_ERROR, errorHandle(e.response()))
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
}