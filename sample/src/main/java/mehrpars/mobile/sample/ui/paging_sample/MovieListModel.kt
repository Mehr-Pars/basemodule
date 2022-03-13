package mehrpars.mobile.sample.ui.paging_sample

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import mehrpars.mobile.basemodule.data.PagerBuilder
import mehrpars.mobile.sample.data.database.AppDatabase
import mehrpars.mobile.sample.data.model.MovieListResponse
import mehrpars.mobile.sample.data.model.entity.Movie
import mehrpars.mobile.sample.data.network.ApiClient
import mehrpars.mobile.sample.data.network.RetrofitConfig
import mehrpars.mobile.sample.ui.paging_sample.custom.MovieListPagingSource
import mehrpars.mobile.sample.ui.paging_sample.custom.MovieListRemoteMediator

// todo: context must not be passed directly to model layer. use DI in real usage
class MovieListModel(context: Context) {
    private val db: AppDatabase by lazy { AppDatabase.getInstance(context) }
    private val client: ApiClient by lazy { RetrofitConfig.createService(ApiClient::class.java) }

    /**
     * uses default RemoteMediator to create Pager object
     * */
    @ExperimentalPagingApi
    fun getMovies(): Pager<Int, Movie> {
        val builder = PagerBuilder<Movie, MovieListResponse>()
        builder.withPageSize(10)
            .withDatabaseQuery { db.movieDao().getMovieList() }
            .withNetworkCall { page -> client.getMovies(page) }
            .withSaveNetworkResultStrategy { response, loadType, _ ->
                db.withTransaction {
                    if (loadType == LoadType.REFRESH)
                        db.movieDao().deleteAll()

                    response?.movieList?.let { db.movieDao().saveAll(it) }
                }
            }
            .withReachedEndStrategy { response, pageCount ->
                response?.totalResults != null && (pageCount * 10) >= response.totalResults
            }
        return builder.build()
    }

    /**
     * implement and use custom RemoteMediator for handling network request and saving data strategy
     * define database query for loading data from database
     * */
    @ExperimentalPagingApi
    fun getMovies2() = Pager(
        config = PagingConfig(10),
        remoteMediator = MovieListRemoteMediator(db, client)
    ) {
        db.movieDao().getMovieList()
    }


    /**
     * uses default PagingSource to create Pager object
     * */
    @ExperimentalPagingApi
    fun getMoviesOnlyFromNetwork(): Pager<Int, Movie> {
        val builder = PagerBuilder<Movie, MovieListResponse>()
        builder.withPageSize(10)
            .withNetworkCall { page -> client.getMovies(page) }
            .withMapNetworkResponseStrategy { response -> response?.movieList ?: listOf() }
            .withReachedEndStrategy { _, pageCount ->
                (pageCount * 10) >= 40
            }

        return builder.build()
    }

    /**
     * implement and use custom PagingSource for handling network request
     * */
    fun getMoviesOnlyFromNetwork2() = Pager(
        config = PagingConfig(10)
    ) {
        MovieListPagingSource(
            client
        )
    }

}

