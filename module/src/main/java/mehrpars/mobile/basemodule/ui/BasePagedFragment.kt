package mehrpars.mobile.basemodule.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import mehrpars.mobile.basemodule.paging.util.Comparable
import mehrpars.mobile.basemodule.paging.util.DefaultLoadStateAdapter
import mehrpars.mobile.basemodule.paging.views.CustomRecyclerLayout

abstract class BasePagedFragment<T : Comparable, B : ViewDataBinding, VM : BaseViewModel>(val layoutRes: Int) :
    BaseFragment<VM>() {
    lateinit var recyclerLayout: CustomRecyclerLayout
    lateinit var pagedListAdapter: BasePagedAdapter<T, B>

    override fun initViewAndBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        recyclerLayout = CustomRecyclerLayout(context!!)
        return recyclerLayout
    }

    override fun initLayoutView() {
        recyclerLayout.setLifecycleOwner(this)
        recyclerLayout.setAdapter(
            pagedListAdapter.withLoadStateFooter(DefaultLoadStateAdapter(pagedListAdapter))
        )

        recyclerLayout.setOnRefreshListener { pagedListAdapter.refresh() }
    }

    override fun initAdapter() {
        pagedListAdapter = object :
            BasePagedAdapter<T, B>(layoutId = layoutRes) {

            override fun onBindView(binding: B, item: T?, position: Int) {
                bindRecyclerItem(binding, item)
            }
        }
    }

    override fun observeViewModelChange(viewModel: VM?) {
        super.observeViewModelChange(viewModel)

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            getPagingDataFlow().collectLatest {
                pagedListAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
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

    abstract fun getPagingDataFlow(): Flow<PagingData<T>>

    abstract fun bindRecyclerItem(binding: B, item: T?)

}