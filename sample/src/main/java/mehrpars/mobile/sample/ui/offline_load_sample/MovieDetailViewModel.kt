package mehrpars.mobile.sample.ui.offline_load_sample

import android.app.Application
import androidx.lifecycle.LiveData
import mehrpars.mobile.basemodule.data.RepositoryLiveData
import mehrpars.mobile.basemodule.data.Result
import mehrpars.mobile.basemodule.getSafeArguments
import mehrpars.mobile.basemodule.ui.BaseViewModel
import mehrpars.mobile.sample.data.model.entity.Movie

class MovieDetailViewModel(application: Application) : BaseViewModel(application) {
    private val model: MovieDetailModel by lazy { MovieDetailModel(application) }
    private val safeArgs by lazy {
        arguments?.getSafeArguments<MovieDetailFragmentArgs>()
    }

    // using repository live data (has also refresh function)
    val movieDetail1: RepositoryLiveData<Movie>? by lazy {
        model.getMovieDetail1(safeArgs?.movieId ?: "")
    }

    // using simple live data
    val movieDetail2: LiveData<Result<Movie>>? by lazy {
        model.getMovieDetail2(safeArgs?.movieId ?: "")
    }

    fun reloadMovieDetail() {
        safeRequest(
            onExecuteAction = {
                movieDetail1?.refresh()
            },
            onCancelAction = {
                movieDetail1?.postValue(Result.error("request canceled by user"))
            }
        )
    }


}