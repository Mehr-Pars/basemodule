package com.m2d.basemodule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.m2d.basemodule.retrofit.ErrorType

abstract class BaseActivity<VM : BaseViewModel?> : AppCompatActivity(), LifecycleOwner {

    protected var viewModel: VM? = null


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
    protected abstract fun initAdapter()

    /**
     *  observe your viewModel's liveData here
     */
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
    }

}