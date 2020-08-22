package mehrpars.mobile.sample.ui.paging_sample

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mehrpars.mobile.basemodule.ui.BaseViewModel
import mehrpars.mobile.sample.data.model.entity.Movie

class MovieListViewModel(application: Application) : BaseViewModel(application) {
    private val model: MovieListModel by lazy { MovieListModel(application) }

    @ExperimentalPagingApi
    fun getMoviesFlow(): Flow<PagingData<Movie>> {
        return model.getMovies().flow
    }

}