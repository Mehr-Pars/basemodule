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
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import mehrpars.mobile.basemodule.paging.util.Comparable
import mehrpars.mobile.basemodule.paging.util.DefaultLoadStateAdapter
import mehrpars.mobile.basemodule.paging.views.CustomRecyclerLayout

abstract class BasePagedFragment<T : Comparable, IB : ViewDataBinding, FB : ViewBinding?, VM : BaseViewModel>(
    val listItemRes: Int
) : Fragment() {

    protected var binding: FB? = null
        private set

    protected var viewModel: VM? = null
        private set

    var recyclerLayout: CustomRecyclerLayout? = null

    lateinit var pagedListAdapter: BasePagedAdapter<T, IB>

    private var job: Job? = null


    /**
     * initialize your viewModel in here
     */
    protected abstract fun onCreateViewModel(): VM

    /**
     * initialize your view binding in here
     */
    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?): FB

    /**
     * initialize your RecyclerLayout
     */
    protected abstract fun initRecyclerLayout(): CustomRecyclerLayout

    /**
     * return data pager for pagination
     */
    protected abstract fun getDataPager(): Flow<PagingData<T>>

    /**
     * bind recyclerview view items
     * */
    protected abstract fun bindRecyclerItem(binding: IB, item: T?, position: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        initAdapter()

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
     * initialize your adapter if you have one
     */
    protected open fun initAdapter() {
        pagedListAdapter = object : BasePagedAdapter<T, IB>(layoutId = listItemRes) {

            override fun onBindView(binding: IB, item: T?, position: Int) {
                bindRecyclerItem(binding, item, position)
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

    /**
     * initialize your views and set listeners in here
     */
    protected open fun bindView(binding: FB) {
        recyclerLayout = initRecyclerLayout()

        recyclerLayout?.setLifecycleOwner(this)
        recyclerLayout?.setAdapter(
            pagedListAdapter.withLoadStateHeaderAndFooter(
                DefaultLoadStateAdapter(pagedListAdapter),
                DefaultLoadStateAdapter(pagedListAdapter)
            )
        )
        recyclerLayout?.setOnRetryListener {
            pagedListAdapter.retry()
            recyclerLayout?.hideRetryLayout()
        }

        recyclerLayout?.setOnRefreshListener { pagedListAdapter.refresh() }
    }

    /**
     * handle load state here (ie, show retry button if load state is Error)
     */
    protected open fun onStateChange(loadState: CombinedLoadStates) {
        recyclerLayout?.swipeRefresh?.isRefreshing = loadState.refresh is LoadState.Loading

        when (loadState.refresh) {
            is LoadState.Loading -> {
                recyclerLayout?.hideRetryLayout()
            }
            is LoadState.Error -> {
                if (pagedListAdapter.itemCount == 0) {
                    recyclerLayout?.showRetryLayout()
                } else {
                    recyclerLayout?.hideRetryLayout()
                }
            }
        }

    }

    /**
     * observe your viewModel's liveData variables whenever they emit any data
     * */
    protected open fun observeViewModelChange(viewModel: VM) {
        loadData()
    }

    open fun loadData() {
        job?.cancel()
        job = lifecycleScope.launchWhenCreated {
            getDataPager().collectLatest {
                pagedListAdapter.submitData(it)
            }
        }
    }

}