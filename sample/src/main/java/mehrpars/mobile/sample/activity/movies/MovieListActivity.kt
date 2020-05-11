package mehrpars.mobile.sample.activity.movies

import android.content.Intent
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_movie_list.*
import mehrpars.mobile.basemodule.ui.BaseActivity
import mehrpars.mobile.sample.CustomLoadMoreView
import mehrpars.mobile.sample.R
import mehrpars.mobile.sample.activity.movieDetail.MovieDetailActivity
import mehrpars.mobile.sample.adapter.AdapterMovieList2
import mehrpars.mobile.sample.api.MovieListModel
import mehrpars.mobile.sample.databinding.ActivityMovieListBinding

class MovieListActivity : BaseActivity<MovieListActivityViewModel>(),
    BaseQuickAdapter.RequestLoadMoreListener {

    lateinit var binding: ActivityMovieListBinding
    lateinit var adapter: AdapterMovieList2
    lateinit var empty_view: View
    lateinit var loading_view: View
    lateinit var load_more_view: View
    private var page = 1
    private var totalPage = 1

    override fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list)
        binding.viewmodel = viewModel
        viewModel?.getMovieList(page)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MovieListActivityViewModel::class.java)

    }

    override fun initViews() {
        recyclerviewMovieList.layoutManager = GridLayoutManager(this, 2)
        empty_view = View.inflate(this, R.layout.empty_view, null)
        loading_view = View.inflate(this, R.layout.loading_view, null)
        load_more_view = View.inflate(this, R.layout.load_more_view, null)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = AdapterMovieList2().apply {
            setEnableLoadMore(true)
            emptyView = loading_view
            setOnLoadMoreListener(this@MovieListActivity, recyclerviewMovieList)
            setOnItemClickListener { adapter, _, position ->
                //go to movie detail
                startActivity(
                    Intent(
                        this@MovieListActivity,
                        MovieDetailActivity::class.java
                    ).apply {
                        putExtra("id", (adapter.data[position] as MovieListModel.Data).id)
                    })
            }
            setLoadMoreView(CustomLoadMoreView())
        }
        recyclerviewMovieList.adapter = adapter
    }

    override fun observeViewModelChange(viewModel: MovieListActivityViewModel?) {
        super.observeViewModelChange(viewModel)

        viewModel?.movieData?.observe(this, Observer {
            if (it.data.isEmpty())
                adapter.loadMoreEnd()
            else {
                page++
                totalPage = it.metadata.pageCount
            }

            if (adapter.data.isEmpty())
                adapter.setNewData(it.data)
            else {
                adapter.addData(it.data)
                adapter.loadMoreComplete()
            }
        })
    }

    override fun onLoadMoreRequested() {
        if (page <= totalPage)
            viewModel?.getMovieList(page)
        else
            adapter.loadMoreEnd()
    }
}
