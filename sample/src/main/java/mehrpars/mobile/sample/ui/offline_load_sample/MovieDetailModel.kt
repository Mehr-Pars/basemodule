package mehrpars.mobile.sample.ui.offline_load_sample

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import mehrpars.mobile.basemodule.data.RepositoryLiveData
import mehrpars.mobile.basemodule.data.Result
import mehrpars.mobile.basemodule.data.repositoryLiveData
import mehrpars.mobile.basemodule.data.resultLiveData
import mehrpars.mobile.sample.data.database.AppDatabase
import mehrpars.mobile.sample.data.model.MovieDetailResponse
import mehrpars.mobile.sample.data.model.entity.Movie
import mehrpars.mobile.sample.data.network.ApiClient
import mehrpars.mobile.sample.data.network.RetrofitConfig

// todo: context must not be passed directly to model layer. use DI in real usage
class MovieDetailModel(context: Context) {
    private val db: AppDatabase by lazy { AppDatabase.getInstance(context) }
    private val client: ApiClient by lazy { RetrofitConfig.createService(ApiClient::class.java) }

    /**
     * function creates RepositoryLiveData which also has "refresh" feature to reload data from remote repo
     * uses database as single source of data and loads fresh data from network.
     * observers will be triggered again after fetched data saved into database.
     * @see RepositoryLiveData.refresh
     * */
    fun getMovieDetail1(movieId: String): RepositoryLiveData<Movie> {
        return repositoryLiveData(
            databaseQuery = { db.movieDao().getMovieDetail(movieId) },
            networkCall = { client.getMovieDetail(movieId) },
            saveCallResult = { db.movieDao().update(it.movie) }
        )
    }

    /**
     * function created Simple LiveData to emit data
     * uses database as single source of data and loads fresh data from network.
     * observers will be triggered again after fetched data saved into database.
     * no refresh action supported, use repositoryLiveData if you need to reload from remote repo
     * */
    fun getMovieDetail2(movieId: String): LiveData<Result<Movie>> {
        return resultLiveData(
            databaseQuery = { db.movieDao().getMovieDetail(movieId) },
            networkCall = { client.getMovieDetail(movieId) },
            saveCallResult = { db.movieDao().update(it.movie) }
        ).distinctUntilChanged()
    }

    /**
     * uses network as the only data source
     * */
    fun getMovieDetailOnlyFromNetwork(movieId: String): LiveData<Result<MovieDetailResponse>> {
        return resultLiveData { client.getMovieDetail(movieId) }
    }

}

