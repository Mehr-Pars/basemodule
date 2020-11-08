package mehrpars.mobile.basemodule.data

/** helpers for using single source of truth base on sample:
 * https://proandroiddev.com/android-architecture-starring-kotlin-coroutines-jetpack-mvvm-room-paging-retrofit-and-dagger-7749b2bae5f7
 * */

import androidx.annotation.CallSuper
import androidx.lifecycle.*
import androidx.paging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Created by Ali Arasteh
 */

// region LiveData Related Functions

/**
 * this class handles remote repository data load
 * also saving data loaded from remote repository
 */
class RemoteRepository<A>(
    private val networkCall: suspend () -> Response<A>,
    private val saveCallResult: suspend (A) -> Unit
) {

    suspend fun load(
        onDone: () -> Unit,
        onError: (message: String?, error: Throwable?) -> Unit
    ) {
        // try loading data from network and handle possible network errors
        val response = getResult { networkCall.invoke() }
        if (response.status == Result.Status.SUCCESS) {
            // save data loaded from network into database
            saveCallResult(response.data!!)
            onDone()
        } else if (response.status == Result.Status.ERROR) {
            onError(response.message, response.error)
        }
    }

}

open class RefreshableLiveData<T> : LiveData<T>() {

    open fun refresh() {}
}

/**
 * this class handles local and remote data sources with SSOT architecture
 * database serves as the single source of truth
 * therefore UI can only receive data updates from database
 * network request updates local database and UI observers will be triggered afterward
 * this class also provides "refresh" function in case remote data reload required
 * @see refresh
 */
class RepositoryLiveData<T>(
    private val databaseQuery: () -> LiveData<T>,
    private val remoteRepository: RemoteRepository<*>
) : RefreshableLiveData<Result<T>>() {
    private var localSource: Source<*>? = null
    private var loadingJob: Job? = null
    private var firstLoad = true

    init {
        loadFromLocal()
    }

    /**
     * function invokes database query and uses returned LiveData as the source for loading data.
     * */
    private fun loadFromLocal() {
        val localLiveData = databaseQuery.invoke().map {
            if (it == null)
                Result.error("Object not found in database")
            else
                Result.success(it)
        }
        localSource = Source(localLiveData) {
            // emit data upstream whenever localSource LiveData emits something
            this@RepositoryLiveData.value = it

            // trigger remote data call if not already triggered
            if (firstLoad) {
                firstLoad = false
                loadFromRemote()
            }
        }
    }

    /**
     * function invokes network call and saves loaded data into database
     * note: no data will be emitted here, because we only have one source for data and it's database.
     * all other sources save and read from database
     * */
    private fun loadFromRemote() {
        // emit loading state
        this@RepositoryLiveData.postValue(Result.loading())

        // run network call in background
        loadingJob = CoroutineScope(Dispatchers.IO).launch {
            remoteRepository.load(
                onDone = { loadingJob = null },
                onError = { message, error ->
                    this@RepositoryLiveData.postValue(Result.error(message, error))
                    loadingJob = null
                }
            )
        }
    }

    /**
     * invokes network request to load data from remote repository
     * use this if data reload required
     * */
    override fun refresh() {
        loadingJob?.let {
            it.cancel()
            loadingJob = null
        }

        loadFromRemote()
    }

    @CallSuper
    override fun onActive() {
        localSource?.plug()
    }

    @CallSuper
    override fun onInactive() {
        localSource?.unplug()
        loadingJob?.cancel()
    }

    public override fun postValue(value: Result<T>?) {
        super.postValue(value)
    }

    public override fun setValue(value: Result<T>?) {
        super.setValue(value)
    }

    private class Source<V> internal constructor(
        val liveData: LiveData<V>,
        val observer: (V?) -> Unit
    ) : Observer<V?> {

        fun plug() {
            liveData.observeForever(this)
        }

        fun unplug() {
            liveData.removeObserver(this)
        }

        override fun onChanged(v: V?) {
            observer.invoke(v)
        }
    }
}

fun <T, A> repositoryLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Response<A>,
    saveCallResult: suspend (A) -> Unit
): RepositoryLiveData<T> {
    val remoteRepository = RemoteRepository(networkCall, saveCallResult)
    return RepositoryLiveData(databaseQuery, remoteRepository)
}


/**
 * the database serves as the single source of truth.
 * therefore UI can receive data updates from database only.
 * function Returns LiveData Instance of data loaded from database.
 * function notify UI about:
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
    val response = getResult { networkCall.invoke() }
    if (response.status == Result.Status.SUCCESS) {
        /* save data loaded from network into database. as we are using LiveData
           observers will be automatically triggered on database change */
        saveCallResult(response.data!!)
    } else if (response.status == Result.Status.ERROR) {
        emit(Result.error<T>(response.message, response.error))
//            emitSource(source)
    }
}

/**
 * the network serves as the single source of truth.
 * function notify UI about:
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
    val response = getResult { networkCall.invoke() }
    if (response.status == Result.Status.SUCCESS) {
        // emit data from network
        emitSource(MutableLiveData(response))
    } else if (response.status == Result.Status.ERROR) {
        emit(Result.error<T>(response.message, response.error))
    }
}
// endregion

// region Pager Related Functions
/**
 * creates Pager for using in paging architecture. creates RemoteMediator
 * the database serves as the single source of truth.
 * therefore UI can receive data updates from database only.
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
            return try {
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

                MediatorResult.Success(endOfPaginationReached = reachedEnd)
            } catch (e: IOException) {
                MediatorResult.Error(e)
            } catch (e: HttpException) {
                MediatorResult.Error(e)
            } catch (e: Exception) {
                MediatorResult.Error(e)
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
            } catch (e: Exception) {
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
        val message = " ${response.code()} ${response.message()}"
        return Result.error("request failure: $message")
    } catch (e: Exception) {
        val message = e.message ?: e.toString()
        return Result.error("request failure: $message", e)
    }
}
// endregion