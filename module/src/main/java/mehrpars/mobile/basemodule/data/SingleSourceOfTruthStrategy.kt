package mehrpars.mobile.basemodule.data

/** helpers for using single source of truth base on sample:
 * https://proandroiddev.com/android-architecture-starring-kotlin-coroutines-jetpack-mvvm-room-paging-retrofit-and-dagger-7749b2bae5f7
 * */

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Created by Ali Arasteh
 */

// region LiveData Related Functions
/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [Result.Status.SUCCESS] - with data from database
 * [Result.Status.ERROR] - if error has occurred from any source
 * [Result.Status.LOADING]
 */
fun <T, A> resultLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Response<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Result<T>> = liveData(Dispatchers.IO) {
    // emit Loading status
    emit(Result.loading<T>())

    // emit data from database
    val localSource = databaseQuery.invoke().map { Result.success(it) }
    emitSource(localSource)

    // try loading data from network and handle possible network errors
    val responseStatus = getResult { networkCall.invoke() }
    if (responseStatus.status == Result.Status.SUCCESS) {
        /* save data loaded from network into database. as we are using LiveData
           observers will be automatically triggered on database change */
        saveCallResult(responseStatus.data!!)
    } else if (responseStatus.status == Result.Status.ERROR) {
        emit(Result.error<T>(responseStatus.message!!))
//            emitSource(source)
    }
}

/**
 * The network serves as the single source of truth.
 * Function notify UI about:
 * [Result.Status.SUCCESS] - with data from network
 * [Result.Status.ERROR] - if error has occurred from any source
 * [Result.Status.LOADING]
 */
fun <T> resultLiveData(
    networkCall: suspend () -> Response<T>
): LiveData<Result<T>> = liveData(Dispatchers.IO) {
    // emit Loading status
    emit(Result.loading<T>())

    // try loading data from network and handle possible network errors
    val responseStatus =
        getResult { networkCall.invoke() }
    if (responseStatus.status == Result.Status.SUCCESS) {
        // emit data from network
        emitSource(MutableLiveData(responseStatus))
    } else if (responseStatus.status == Result.Status.ERROR) {
        emit(Result.error<T>(responseStatus.message!!))
    }
}
// endregion

// region Pager Related Functions
/**
 * Creates Pager for using in paging architecture. creates RemoteMediator
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * @param pageSize: number of items loaded per page
 * @param databaseQuery: query for loading data from database
 * @param networkCall: network call for loading data from network
 * @param saveCallResult: this function is meant to save network result into database
 * @param reachedEndStrategy: this function checks when last page reached
 *  */
@ExperimentalPagingApi
fun <T : Any, A> resultPager(
    pageSize: Int = 10,
    databaseQuery: () -> PagingSource<Int, T>,
    networkCall: suspend (page: Int) -> A,
    saveCallResult: suspend (A, LoadType) -> Unit,
    reachedEndStrategy: (A, page: Int) -> Boolean
): Pager<Int, T> = Pager(
    config = PagingConfig(pageSize),
    remoteMediator = object : RemoteMediator<Int, T>() {
        private var pageCount = -1
        private var reachedEnd = false

        override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
            try {
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageCount = 1
                    }
                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    LoadType.APPEND -> {
                        if (reachedEnd) return MediatorResult.Success(endOfPaginationReached = true)
                    }
                }

                // try loading data from network
                val response = networkCall(pageCount)

                // save network result
                saveCallResult(response, loadType)

                pageCount++

                // check if reached last page
                reachedEnd = reachedEndStrategy.invoke(response, pageCount)

                return MediatorResult.Success(endOfPaginationReached = reachedEnd)
            } catch (e: IOException) {
                return MediatorResult.Error(e)
            } catch (e: HttpException) {
                return MediatorResult.Error(e)
            }
        }

    }
) {
    databaseQuery.invoke()
}

/**
 * Creates Pager for using in paging architecture, creates PagingSource
 * The network serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * @param pageSize: number of items loaded per page
 * @param networkCall: network call for loading data from network
 * @param mapResponse: maps response to desire object list
 *  */
fun <T : Any, A> resultPager(
    pageSize: Int = 10,
    networkCall: suspend (page: Int) -> A,
    mapResponse: (A) -> List<T>
): Pager<Int, T> = Pager(
    config = PagingConfig(pageSize)
) {
    object : PagingSource<Int, T>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
            return try {
                val pageNumber = params.key ?: 1

                // try loading data from network
                val response = networkCall(pageNumber)

                LoadResult.Page(
                    data = mapResponse.invoke(response),
                    prevKey = null,
                    nextKey = pageNumber + 1
                )
            } catch (e: IOException) {
                LoadResult.Error(e)
            } catch (e: HttpException) {
                LoadResult.Error(e)
            }
        }
    }
}
// endregion

// region Helper Functions
suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
    try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Result.success(body)
        }
        return error(" ${response.code()} ${response.message()}")
    } catch (e: Exception) {
        return error(
            e.message ?: e.toString()
        )
    }
}

fun <T> error(message: String): Result<T> {
    Log.e("Error", message)
    return Result.error("network call failure: $message")
}
// endregion