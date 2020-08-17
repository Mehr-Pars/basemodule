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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import mehrpars.mobile.basemodule.R

abstract class BaseFragment<VM : BaseViewModel?> : Fragment() {
    protected var viewModel: VM? = null
    private var networkErrorDialogShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        arguments?.let { handleArguments(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return initViewAndBinding(inflater, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()

        initLayoutView()

        observeViewModelChange(viewModel)
    }

    /**
     * initialize your viewModel in here
     */
    protected abstract fun initViewModel()

    /**
     * get your arguments here
     */
    @CallSuper
    open fun handleArguments(arguments: Bundle) {
        viewModel?.handleArguments(arguments)
    }

    /**
     * Inflate view in function
     *
     * make binding object of your class then
     * initialize binding in here
     *
     * do not forget to add binding in your xml layout
     */
    protected abstract fun initViewAndBinding(inflater: LayoutInflater, container: ViewGroup?): View

    /**
     * initialize your adapter(s) here then assign to a recycler or viewpager
     */
    protected open fun initAdapter() {}

    /**
     * If you want init view set in this function
     */
    protected abstract fun initLayoutView()

    /**
     *  observe your viewModel's liveData here
     */
    @CallSuper
    protected open fun observeViewModelChange(viewModel: VM?) {
        viewModel?.error?.observe(viewLifecycleOwner, Observer {
            handleError(it)
        })
    }

    /**
     *  handle errors passed from ViewModel (ie, network errors etc)
     */
    @CallSuper
    open fun handleError(error: BaseViewModel.Error) {
        if (error.type == BaseViewModel.ErrorType.CONNECTION_ERROR) {
            showNetworkErrorDialog()
        }
    }

    private fun showNetworkErrorDialog() {
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