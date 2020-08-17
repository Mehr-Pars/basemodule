package mehrpars.mobile.sample.ui.paging_sample

import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mehrpars.mobile.basemodule.ui.BasePagedFragment
import mehrpars.mobile.sample.R
import mehrpars.mobile.sample.data.model.entity.Movie
import mehrpars.mobile.sample.databinding.ListItemMovieBinding


class MovieListFragment :
    BasePagedFragment<Movie, ListItemMovieBinding, MovieListViewModel>(R.layout.list_item_movie) {

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)
    }

    override fun bindRecyclerItem(binding: ListItemMovieBinding, item: Movie?) {
        binding.movie = item
    }

    override fun getPagingDataFlow(): Flow<PagingData<Movie>> {
        return viewModel!!.getMoviesFlow()
    }

}
