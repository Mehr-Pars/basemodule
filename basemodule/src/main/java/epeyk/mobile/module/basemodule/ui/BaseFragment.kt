package epeyk.mobile.module.basemodule.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import epeyk.mobile.module.basemodule.R
import epeyk.mobile.module.basemodule.data.network.retrofit.ErrorType

abstract class BaseFragment<VM : BaseViewModel?> : Fragment() {

    protected var viewModel: VM? = null
    private var networkErrorDialogShown = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initArguments()
        initViewModel()
        initLayoutView()
        initAdapter()
        observeViewModelChange(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return initViewAndBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    /**
     * Inflate view in function
     *
     * make binding object of your class then
     * initialize binding in here
     *
     * dont forget to add binding in your xml layout
     */
    protected abstract fun initViewAndBinding(inflater: LayoutInflater, container: ViewGroup?): View


    /**
     * get your arguments here
     */
    protected abstract fun initArguments()


    /**
     * initialize your viewModel in here
     */
    protected abstract fun initViewModel()


    /**
     * If you want init view set in this function
     */
    protected abstract fun initLayoutView()

    /**
     * initialize your adapter(s) here then assign to a recycler or viewpager
     */
    @Deprecated("init adapter directly in viewModel")
    open fun initAdapter(){}

    @CallSuper
    protected open fun observeViewModelChange(viewModel: VM?) {
        viewModel?.error?.observe(this, Observer {
            Log.v("masood", "BaseFragment error: " + it?.second?.message)
            if (it?.first == ErrorType.HTTP_ERROR)
                it.second?.let { errorHttp ->
                    //                    showErrorDialog(it.first, errorHttp)
                }
            else {
//                showErrorDialog(it!!.first, null)
            }
        })

        viewModel?.networkError?.observe(this, Observer { hasError->
            if (hasError)
                showNetworkErrorDialog()
        })
    }

    private fun showNetworkErrorDialog() {
        if (!networkErrorDialogShown && context != null) {
            networkErrorDialogShown = true

            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_network_error)

            dialog.findViewById<TextView>(R.id.cancel).setOnClickListener {
                networkErrorDialogShown = false
                viewModel?.cancelRequestQueue()
                dialog.dismiss()
            }
            dialog.findViewById<TextView>(R.id.retry).setOnClickListener {
                networkErrorDialogShown = false
                viewModel?.retryOnRequestQueue()
                dialog.dismiss()
            }

            dialog.show()
        }
    }

}