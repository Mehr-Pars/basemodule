package mehrpars.mobile.sample.ui.paging_sample

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mehrpars.mobile.basemodule.ui.BaseViewModel
import mehrpars.mobile.sample.data.database.AppDatabase
import mehrpars.mobile.sample.data.model.entity.Movie
import mehrpars.mobile.sample.data.network.ApiClient
import mehrpars.mobile.sample.data.network.RetrofitConfig

class MovieListViewModel(application: Application) : BaseViewModel(application) {
    private val model: MovieListModel by lazy { MovieListModel(application) }

    fun getMoviesFlow(): Flow<PagingData<Movie>> {
        return model.getMovies().flow
    }

}