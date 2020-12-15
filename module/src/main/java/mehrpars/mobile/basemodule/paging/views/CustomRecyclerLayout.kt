package mehrpars.mobile.basemodule.paging.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.layout_custom_recycler.view.*
import kotlinx.android.synthetic.main.layout_retry.view.*
import mehrpars.mobile.basemodule.R

/**
 * custom view including recyclerview, swipeRefresh and also empty view to be shown in case
 * recycler list is empty.
 * */
class CustomRecyclerLayout : FrameLayout, LifecycleObserver {
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var emptyLayout: View
    lateinit var retryLayout: View

    /**
     * observer for adapter data change. checks if list has no items shows empty view
     * */
    private val mObserver: RecyclerView.AdapterDataObserver by lazy {
        object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                recyclerView.adapter?.let {
                    if (it.itemCount > 0) {
                        emptyLayout.visibility = View.GONE
                    } else if (!swipeRefresh.isRefreshing) {
                        emptyLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        val rootLayout = View.inflate(context, R.layout.layout_custom_recycler, this)

        emptyLayout = rootLayout.emptyLayout
        retryLayout = rootLayout.retryLayout
        recyclerView = rootLayout.recyclerView
        swipeRefresh = rootLayout.swipeRefresh

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorGreen)
    }

    /**
     * set retry layout visibility to visible
     * */
    fun showRetryLayout() {
        if (!retryLayout.isVisible)
            retryLayout.isVisible = true
    }

    /**
     * set retry layout visibility to invisible
     * */
    fun hideRetryLayout() {
        if (retryLayout.isVisible)
            retryLayout.isVisible = false
    }

    /**
     * set RecyclerView adapter and registers data change observer
     * */
    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
        adapter.registerAdapterDataObserver(mObserver)
    }

    /**
     * set RecyclerView layout manager
     * */
    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    /**
     * set action for SwipeToRefresh onRefresh event
     * */
    fun setOnRefreshListener(onRefresh: () -> Unit) {
        swipeRefresh.setOnRefreshListener { onRefresh() }
    }

    /**
     * set action for retry event
     * */
    fun setOnRetryListener(onRetry: () -> Unit) {
        retryButton.setOnClickListener { onRetry() }
    }

    /**
     * set life cycle owner to make view life cycle aware
     * */
    fun setLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
    }

    /**
     * unregister data change observer to avoid possible crashes
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        recyclerView.adapter?.unregisterAdapterDataObserver(mObserver)
    }

}