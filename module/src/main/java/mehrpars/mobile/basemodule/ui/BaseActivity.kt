package mehrpars.mobile.basemodule.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import ly.count.android.sdk.Countly
import mehrpars.mobile.basemodule.BaseApp
import mehrpars.mobile.basemodule.R
import mehrpars.mobile.basemodule.data.network.retrofit.ErrorType
import mehrpars.mobile.basemodule.utils.LocaleUtils

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

        initViewModel()
        handleIntent(intent)
        initBinding()
        initViews()

        observeViewModelChange(viewModel)
    }

    /**
     * get your passed intent here
     */
    @CallSuper
    open fun handleIntent(intent: Intent) {
        viewModel?.handleIntent(intent)
    }

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

    override fun onStart() {
        super.onStart()
        if (Countly.sharedInstance().isInitialized) // set countly onStart for accurate application session tracking.
            Countly.sharedInstance().onStart(this)
    }

    override fun onStop() {
        if (Countly.sharedInstance().isInitialized) // set countly onStop for accurate application session tracking.
            Countly.sharedInstance().onStop()
        super.onStop()
    }

}