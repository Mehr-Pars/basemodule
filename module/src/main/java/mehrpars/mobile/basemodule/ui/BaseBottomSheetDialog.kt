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
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mehrpars.mobile.basemodule.R


abstract class BaseBottomSheetDialog<VM : BaseViewModel?> : BottomSheetDialogFragment() {
    var viewModel: VM? = null
    private var networkErrorDialogShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)

        initViewModel()

        arguments?.let { handleArguments(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return initViewAndBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.viewTreeObserver.addOnGlobalLayoutListener {
//
//            val d = dialog as BottomSheetDialog
//            val bottomSheetInternal = d.findViewById<View>(R.id.design_bottom_sheet)
//            val behavior = BottomSheetBehavior.from(bottomSheetInternal!!)
//            behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            behavior.skipCollapsed = false
//
//            bottomSheetInternal.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//            bottomSheetInternal.minimumHeight = context!!.resources.displayMetrics.heightPixels
//
//            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                    }
//                }
//
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                }
//            })
//        }
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
    private fun handleArguments(arguments: Bundle) {
        viewModel?.handleArguments(arguments)
    }

    /**
     * Inflate view in function
     *
     * make binding object of your class then
     * initialize binding in here
     *
     * dont forget to add binding in your xml layout
     */
    protected abstract fun initViewAndBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View?


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