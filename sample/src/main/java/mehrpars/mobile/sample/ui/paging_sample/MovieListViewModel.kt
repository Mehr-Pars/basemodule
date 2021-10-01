package mehrpars.mobile.sample.ui.paging_sample

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import mehrpars.mobile.basemodule.ui.BaseViewModel
import mehrpars.mobile.sample.data.model.entity.Movie

class MovieListViewModel(application: Application) : BaseViewModel(application) {
    private val model: MovieListModel by lazy { MovieListModel(application) }

    @ExperimentalPagingApi
    private val moviePager by lazy {
        model.getMovies().flow.cachedIn(viewModelScope)
    }

    @ExperimentalPagingApi
    fun getMoviesPager(): Flow<PagingData<Movie>> {
        return moviePager
    }

}