package mehrpars.mobile.basemodule.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import mehrpars.mobile.basemodule.BaseApp
import mehrpars.mobile.basemodule.data.error.GeneralError
import mehrpars.mobile.basemodule.utils.EventObserver
import mehrpars.mobile.basemodule.utils.LocaleUtils

abstract class BaseActivity<VM : BaseViewModel?, VB : ViewBinding?> :
    AppCompatActivity(), LifecycleOwner {

    protected var binding: VB? = null
        private set

    protected var viewModel: VM? = null
        private set

    /**
     * set app default app locale if Locale initialized
     */
    init {
        BaseApp.appLocale?.let { LocaleUtils.updateConfig(this) }
    }

    /**
     * initialize your viewModel in here
     */
    protected abstract fun onCreateViewModel(): VM

    /**
     * initialize your view binding in here
     */
    protected abstract fun onCreateViewBinding(inflater: LayoutInflater): VB


    /**
     * do any data binding related action in here
     */
    protected abstract fun bindView(binding: VB)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = onCreateViewModel()

        handleIntent(intent)

        binding = onCreateViewBinding(layoutInflater)

        binding?.let {
            setContentView(it.root)
            bindView(it)
        }

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
        // ignored by default
    }

}