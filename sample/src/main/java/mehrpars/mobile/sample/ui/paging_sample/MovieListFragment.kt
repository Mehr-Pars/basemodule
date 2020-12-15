package mehrpars.mobile.sample.ui.paging_sample

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import mehrpars.mobile.basemodule.safeNavigate
import mehrpars.mobile.basemodule.ui.BasePagedFragment
import mehrpars.mobile.sample.R
import mehrpars.mobile.sample.data.model.entity.Movie
import mehrpars.mobile.sample.databinding.FragmentPaggingSampleBinding
import mehrpars.mobile.sample.databinding.ListItemMovieBinding


class MovieListFragment :
    BasePagedFragment<Movie, ListItemMovieBinding, FragmentPaggingSampleBinding, MovieListViewModel>(
        R.layout.fragment_pagging_sample,
        R.id.recyclerLayoutView,
        R.layout.list_item_movie
    ) {

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)
    }

    override fun bindRecyclerItem(binding: ListItemMovieBinding, item: Movie?) {
        binding.movie = item
        binding.container.setOnClickListener {
            findNavController().safeNavigate(
                R.id.movie_list,
                MovieListFragmentDirections.actionMovieToDetail(item?.id ?: "")
            )
        }
    }

    @ExperimentalPagingApi
    override fun getDataPager(): Pager<Int, Movie> {
        return viewModel!!.getMoviesPager()
    }

    override fun fragmentLayoutBindView(binding: FragmentPaggingSampleBinding) {

    }

}
