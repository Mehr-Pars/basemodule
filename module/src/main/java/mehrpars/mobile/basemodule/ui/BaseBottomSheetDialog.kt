package mehrpars.mobile.basemodule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mehrpars.mobile.basemodule.R
import mehrpars.mobile.basemodule.data.error.GeneralError
import mehrpars.mobile.basemodule.utils.EventObserver


abstract class BaseBottomSheetDialog<VM : BaseViewModel?, VB : ViewBinding?>() :
    BottomSheetDialogFragment() {

    protected var binding: VB? = null
        private set

    protected var viewModel: VM? = null
        private set


    /**
     * initialize your viewModel in here
     */
    protected abstract fun onCreateViewModel(): VM

    /**
     * initialize your view binding in here
     */
    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB


    /**
     * do any data binding related action in here
     */
    protected abstract fun bindView(binding: VB)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)

        viewModel = onCreateViewModel()

        arguments?.let { handleArguments(it) }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = onCreateViewBinding(inflater, container)

        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let { bindView(it) }

        viewModel?.let { observeViewModelChange(it) }

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
        // ignored by default
    }

}