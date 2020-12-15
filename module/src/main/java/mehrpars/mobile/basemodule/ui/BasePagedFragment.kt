package mehrpars.mobile.basemodule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import kotlinx.coroutines.flow.collectLatest
import mehrpars.mobile.basemodule.paging.util.Comparable
import mehrpars.mobile.basemodule.paging.util.DefaultLoadStateAdapter
import mehrpars.mobile.basemodule.paging.views.CustomRecyclerLayout

abstract class BasePagedFragment<T : Comparable, B : ViewDataBinding, VM : BaseViewModel>(val listItemRes: Int) :
    Fragment() {
    protected var viewModel: VM? = null
    lateinit var recyclerLayout: CustomRecyclerLayout
    lateinit var pagedListAdapter: BasePagedAdapter<T, B>

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        arguments?.let { handleArguments(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        recyclerLayout = CustomRecyclerLayout(requireContext())
        return recyclerLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()

        initView()

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
        recyclerLayout.setOnRetryListener {
            pagedListAdapter.retry()
            recyclerLayout.hideRetryLayout()
        }

        recyclerLayout.setOnRefreshListener { pagedListAdapter.refresh() }
    }

    /**
     * handle load state here (ie, show retry button if load state is Error)
     */
    protected open fun onStateChange(loadState: CombinedLoadStates) {
        recyclerLayout.swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
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
            pagedListAdapter.loadStateFlow.collectLatest { loadState ->
                onStateChange(loadState)
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