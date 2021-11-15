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
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import mehrpars.mobile.basemodule.BaseApp
import mehrpars.mobile.basemodule.R
import mehrpars.mobile.basemodule.data.error.GeneralError
import mehrpars.mobile.basemodule.data.error.NetworkError
import mehrpars.mobile.basemodule.utils.EventObserver
import mehrpars.mobile.basemodule.utils.LocaleUtils

abstract class BaseActivity<VM : BaseViewModel?, B : ViewDataBinding>(private val layoutId: Int) :
    AppCompatActivity(), LifecycleOwner {

    protected var viewModel: VM? = null
    protected lateinit var binding: B
    private var networkErrorDialogShown = false

    /**
     * set app default app locale if Locale initialized
     */
    init {
        BaseApp.appLocale?.let { LocaleUtils.updateConfig(this) }
    }

    /**
     * initialize your viewModel in here
     */
    protected abstract fun initViewModel()

    /**
     * do any data binding related action in here
     */
    protected abstract fun bindView(binding: B)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        handleIntent(intent)

        initBinding()

        bindView(binding)

        observeViewModelChange(viewModel)
    }

    /**
     * get your passed intent here
     */
    @CallSuper
    protected open fun handleIntent(intent: Intent) {
        viewModel?.handleIntent(intent)
    }

    /**
     * make binding object of your class then
     * initialize binding in here
     *
     * do not forget to add binding in your xml layout
     */
    protected open fun initBinding() {
        binding = DataBindingUtil.setContentView(this, layoutId!!)
        binding.lifecycleOwner = this
    }

    /**
     *  observe your viewModel's liveData here
     */
    protected open fun observeViewModelChange(viewModel: VM?) {
        viewModel?.generalError?.observe(this, EventObserver { errorList ->
            errorList?.forEach { error -> handleError(error) }
        })
    }

    /**
     *  handle errors passed from ViewModel (ie, network errors etc)
     */
    protected open fun handleError(error: GeneralError) {
        if (error is NetworkError) showNetworkErrorDialog()
    }

    protected fun showNetworkErrorDialog() {
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