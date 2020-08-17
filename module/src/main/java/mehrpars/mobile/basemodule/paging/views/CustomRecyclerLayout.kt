package mehrpars.mobile.basemodule.paging.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.layout_custom_recycler.view.*
import mehrpars.mobile.basemodule.R

class CustomRecyclerLayout : FrameLayout, LifecycleObserver {
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var emptyLayout: View

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
        recyclerView = rootLayout.recyclerView
        swipeRefresh = rootLayout.swipeRefresh

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorGreen)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
        adapter.registerAdapterDataObserver(mObserver)
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    fun setOnRefreshListener(onRefresh: () -> Unit) {
        swipeRefresh.setOnRefreshListener { onRefresh() }
    }

    fun setLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        recyclerView.adapter?.unregisterAdapterDataObserver(mObserver)
    }

}