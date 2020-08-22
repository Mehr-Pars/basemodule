package mehrpars.mobile.sample.ui.offline_load_sample

import android.app.Application
import androidx.lifecycle.LiveData
import mehrpars.mobile.basemodule.data.Result
import mehrpars.mobile.basemodule.getSafeArguments
import mehrpars.mobile.basemodule.ui.BaseViewModel
import mehrpars.mobile.sample.data.model.entity.Movie

class MovieDetailViewModel(application: Application) : BaseViewModel(application) {
    private val model: MovieDetailModel by lazy { MovieDetailModel(application) }
    private val safeArgs by lazy {
        arguments?.getSafeArguments<MovieDetailFragmentArgs>()
    }
    val movieDetail: LiveData<Result<Movie>>? by lazy {
        model.getMovieDetail(safeArgs?.movieId ?: "")
    }
}