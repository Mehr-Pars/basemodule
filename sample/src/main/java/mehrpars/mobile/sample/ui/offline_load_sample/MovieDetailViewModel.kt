package mehrpars.mobile.sample.ui.offline_load_sample

import android.app.Application
import mehrpars.mobile.basemodule.data.RefreshableLiveData
import mehrpars.mobile.basemodule.data.RepositoryLiveData
import mehrpars.mobile.basemodule.data.Result
import mehrpars.mobile.basemodule.getSafeArguments
import mehrpars.mobile.basemodule.ui.BaseViewModel
import mehrpars.mobile.sample.data.model.entity.Movie

class MovieDetailViewModel(application: Application) : BaseViewModel(application) {
    private val model: MovieDetailModel by lazy { MovieDetailModel(application) }
    private val movieId by lazy {
        val args: MovieDetailFragmentArgs? = arguments?.getSafeArguments()
        args?.movieId ?: ""
    }

    // using RefreshableLiveData (has also refresh function)
    private val _movieDetail: RepositoryLiveData<Movie>? by lazy { model.getMovieDetail1(movieId) }
    val movieDetail: RefreshableLiveData<Result<Movie>>? by lazy { _movieDetail }

}