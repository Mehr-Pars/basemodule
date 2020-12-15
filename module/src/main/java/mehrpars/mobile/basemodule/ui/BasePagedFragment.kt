package mehrpars.mobile.basemodule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import kotlinx.coroutines.flow.collectLatest
import mehrpars.mobile.basemodule.paging.util.Comparable
import mehrpars.mobile.basemodule.paging.util.DefaultLoadStateAdapter
import mehrpars.mobile.basemodule.paging.views.CustomRecyclerLayout

abstract class BasePagedFragment<T : Comparable, B : ViewDataBinding, FB : ViewDataBinding, VM : BaseViewModel>(
    val fragmentLayoutRes: Int,
    val recyclerId: Int,
    val listItemRes: Int
) :
    Fragment() {
    protected var viewModel: VM? = null
    lateinit var recyclerLayout: CustomRecyclerLayout
    lateinit var pagedListAdapter: BasePagedAdapter<T, B>
    protected lateinit var fragmentLayoutBinding: FB

    /**
     * do any data binding related action in here
     */
    protected abstract fun fragmentLayoutBindView(binding: FB)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        arguments?.let { handleArguments(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        fragmentLayoutBinding =
            DataBindingUtil.inflate(inflater, fragmentLayoutRes, container, false)

        recyclerLayout = fragmentLayoutBinding.root.findViewById(recyclerId)

        return fragmentLayoutBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()

        initView()

        fragmentLayoutBindView(fragmentLayoutBinding)

        viewModel?.let { observeViewModelChange(it) }
    }

    /**
     * initialize your viewModel in here
     */
    protected abstract fun initViewModel()

    /**
     * return data pager for pagination
     */
    protected abstract fun getDataPager(): Pager<Int, T>

    /**
     * bind recyclerview view items
     * */
    protected abstract fun bindRecyclerItem(binding: B, item: T?)

    /**
     * get your arguments here
     */
    @CallSuper
    protected open fun handleArguments(arguments: Bundle) {
        viewModel?.handleArguments(arguments)
    }


    /**
     * initialize your adapter if you have one
     */
    protected open fun initAdapter() {
        pagedListAdapter = object : BasePagedAdapter<T, B>(layoutId = listItemRes) {

            override fun onBindView(binding: B, item: T?, position: Int) {
                bindRecyclerItem(binding, item)
            }
        }
    }

    /**
     * initialize your views and set listeners in here
     */
    protected open fun initView() {
        recyclerLayout.setLifecycleOwner(this)
        recyclerLayout.setAdapter(
            pagedListAdapter.withLoadStateFooter(DefaultLoadStateAdapter(pagedListAdapter))
        )

        recyclerLayout.setOnRefreshListener { pagedListAdapter.refresh() }
    }

    /**
     * observe your viewModel's liveData variables whenever they emit any data
     * */
    protected open fun observeViewModelChange(viewModel: VM) {

        lifecycleScope.launchWhenCreated {
            getDataPager().flow.collectLatest {
                pagedListAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            pagedListAdapter.loadStateFlow.collectLatest { loadStates ->
                recyclerLayout.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

//        lifecycleScope.launchWhenCreated {
//            @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
//            pagedListAdapter.dataRefreshFlow.collectLatest {
//                recyclerLayout.recyclerView.scrollToPosition(0)
//            }
//        }
    }

}