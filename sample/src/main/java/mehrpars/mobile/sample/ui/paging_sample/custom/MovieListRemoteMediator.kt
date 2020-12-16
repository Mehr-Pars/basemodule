package mehrpars.mobile.sample.ui.paging_sample.custom

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import mehrpars.mobile.sample.data.database.AppDatabase
import mehrpars.mobile.sample.data.database.dao.MovieDao
import mehrpars.mobile.sample.data.model.entity.Movie
import mehrpars.mobile.sample.data.network.ApiClient
import retrofit2.HttpException
import java.io.IOException

/**
 * paged keyed remote mediator
 * */
@OptIn(ExperimentalPagingApi::class)
class MovieListRemoteMediator(private val db: AppDatabase, private val apiClient: ApiClient) :
    RemoteMediator<Int, Movie>() {
    private val movieDao: MovieDao = db.movieDao()
    private val pageSize = 10
    private var pageCount = -1
    private var reachedEnd = false

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        try {
            when (loadType) {
                REFRESH -> {
                    pageCount = 1
                }
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    if (reachedEnd)
                        return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val data = apiClient.getMovies(pageCount)

            db.withTransaction {
                if (loadType == REFRESH)
                    movieDao.deleteAll()

                movieDao.saveAll(data.movieList)
                pageCount++
            }

            reachedEnd = (pageCount * pageSize) >= data.totalResults
            return MediatorResult.Success(endOfPaginationReached = reachedEnd)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
