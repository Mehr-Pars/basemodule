package epeyk.mobile.module.basemodule.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import epeyk.mobile.module.basemodule.BaseApp
import epeyk.mobile.module.basemodule.R
import epeyk.mobile.module.basemodule.data.network.retrofit.ErrorType
import epeyk.mobile.module.basemodule.utils.LocaleUtils

abstract class BaseActivity<VM : BaseViewModel?> : AppCompatActivity(), LifecycleOwner {
    protected var viewModel: VM? = null
    private var networkErrorDialogShown = false

    /**
     * set app default app locale if Locale initialized
     */
    init {
        BaseApp.appLocale?.let { LocaleUtils.updateConfig(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getBundle()
        initViewModel()
        initBinding()
        initViews()
        initAdapter()

        observeViewModelChange(viewModel)
    }

    /**
     * get your intent bundle here
     */
    protected abstract fun getBundle()

    /**
     * make binding object of your class then
     * initialize binding in here
     *
     * dont forget to add binding in your xml layout
     */
    protected abstract fun initBinding()

    /**
     * initialize your viewModel in here
     */
    protected abstract fun initViewModel()

    /**
     * initialize your views in here
     */
    protected abstract fun initViews()

    /**
     * initialize your adapter(s) here then assign to a recycler or viewpager
     */
    @Deprecated("init adapter directly in viewModel")
    open fun initAdapter() {
    }

    /**
     *  observe your viewModel's liveData here
     */
    @CallSuper
    protected open fun observeViewModelChange(viewModel: VM?) {
        viewModel?.error?.observe(this, Observer {
            // showErrorDialog(it)
            if (it?.first == ErrorType.HTTP_ERROR)
                it.second?.let { errorHttp ->
                    //                    showErrorDialog(it.first, errorHttp)
                }
            else {
//                showErrorDialog(it!!.first, null)
            }
        })

        viewModel?.networkError?.observe(this, Observer { hasError ->
            if (hasError) showNetworkErrorDialog()
        })
    }

    private fun showNetworkErrorDialog() {
        if (!networkErrorDialogShown) {
            networkErrorDialogShown = true

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_network_error)

            dialog.findViewById<View>(R.id.cancel).setOnClickListener {
                networkErrorDialogShown = false
                viewModel?.cancelRequestQueue()
                dialog.dismiss()
            }
            dialog.findViewById<View>(R.id.retry).setOnClickListener {
                networkErrorDialogShown = false
                viewModel?.retryOnRequestQueue()
                dialog.dismiss()
            }

            dialog.show()
        }
    }

}