package mehrpars.mobile.basemodule.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import mehrpars.mobile.basemodule.R
import mehrpars.mobile.basemodule.data.error.GeneralError
import mehrpars.mobile.basemodule.data.error.NetworkError
import mehrpars.mobile.basemodule.utils.EventObserver

abstract class BaseFragment<VM : BaseViewModel?, B : ViewDataBinding>(private val layoutId: Int) :
    Fragment() {

    protected var viewModel: VM? = null
    protected lateinit var binding: B
    private var networkErrorDialogShown = false

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

        arguments?.let { handleArguments(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId!!, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindView(binding)

        observeViewModelChange(viewModel)
    }

    /**
     * get your arguments here
     */
    @CallSuper
    protected open fun handleArguments(arguments: Bundle) {
        viewModel?.handleArguments(arguments)
    }

    /**
     *  observe your viewModel's liveData here
     */
    @CallSuper
    protected open fun observeViewModelChange(viewModel: VM?) {
        viewModel?.generalError?.observe(viewLifecycleOwner, EventObserver { errorList ->
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
        if (!networkErrorDialogShown && context != null) {
            networkErrorDialogShown = true

            val dialog = Dialog(requireContext())
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