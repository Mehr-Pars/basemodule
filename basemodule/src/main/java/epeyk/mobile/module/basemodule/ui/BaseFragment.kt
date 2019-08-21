package epeyk.mobile.module.basemodule.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import epeyk.mobile.module.basemodule.data.network.retrofit.ErrorType

abstract class BaseFragment<VM : BaseViewModel?> : Fragment() {

    protected var viewModel: VM? = null


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
    }

}