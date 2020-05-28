package mehrpars.mobile.sample.activity.dataBaseSample.list

import android.content.Intent
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_movie_list_with_like.*
import mehrpars.mobile.basemodule.ui.BaseActivity
import mehrpars.mobile.sample.CustomLoadMoreView
import mehrpars.mobile.sample.R
import mehrpars.mobile.sample.activity.movieDetail.MovieDetailActivity
import mehrpars.mobile.sample.adapter.AdapterMovieListWithLike
import mehrpars.mobile.sample.api.MovieListModel
import mehrpars.mobile.sample.databinding.ActivityMovieListWithLikeBinding
import mehrpars.mobile.sample.db.model.MovieLikeModelDB

class MovieListWithLike : BaseActivity<MovieListWithLikeViewModel>(),
    BaseQuickAdapter.RequestLoadMoreListener {

    lateinit var binding: ActivityMovieListWithLikeBinding
    lateinit var adapter: AdapterMovieListWithLike
    lateinit var empty_view: View
    lateinit var loading_view: View
    lateinit var load_more_view: View
    private var page = 1
    private var totalPage = 1
    lateinit var likedList: MutableList<MovieLikeModelDB>

    override fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list_with_like)
        binding.viewmodel = viewModel
        viewModel?.getMovieList(page)
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MovieListWithLikeViewModel::class.java)
        empty_view = View.inflate(this, R.layout.empty_view, null)
        load_more_view = View.inflate(this, R.layout.load_more_view, null)

    }

    override fun initAdapter() {
        loading_view = View.inflate(this, R.layout.loading_view, null)
        adapter = AdapterMovieListWithLike().apply {
            setEnableLoadMore(true)
            emptyView = loading_view
            setOnLoadMoreListener(this@MovieListWithLike, recyclerViewMovieListWithLink)
            setOnItemClickListener { adapter, _, position ->
                //go to movie detail
                startActivity(
                    Intent(
                        this@MovieListWithLike,
                        MovieDetailActivity::class.java
                    ).apply {
                        putExtra("id", (adapter.data[position] as MovieListModel.Data).id)
                    })
            }
            setOnItemChildClickListener { adapter, view, position ->
                likeVideo(position)
            }
            setLoadMoreView(CustomLoadMoreView())
        }
    }

    override fun initViews() {
        recyclerViewMovieListWithLink.layoutManager = GridLayoutManager(this, 2)
        likedList = mutableListOf()
        recyclerViewMovieListWithLink.adapter = adapter
    }


    override fun observeViewModelChange(viewModel: MovieListWithLikeViewModel?) {
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

    fun likeVideo(position: Int) {
        adapter.data[position].liked = !adapter.data[position].liked
        if (adapter.data[position].liked) {
            viewModel?.likeMovie(adapter.data[position].id)
        } else {
            viewModel?.unlikeMovie(adapter.data[position].id)
        }
        adapter.notifyItemChanged(position)
    }

}
