package mehrpars.mobile.sample.ui.paging_sample

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import mehrpars.mobile.sample.data.database.AppDatabase
import mehrpars.mobile.sample.data.network.ApiClient
import mehrpars.mobile.sample.data.network.RetrofitConfig

class MovieListModel(context: Context) {
    private val db: AppDatabase by lazy { AppDatabase.getInstance(context) }
    private val client: ApiClient by lazy { RetrofitConfig.createService(ApiClient::class.java) }

    fun getMovies() = Pager(
        config = PagingConfig(10),
        remoteMediator = MovieListRemoteMediator(db, client)
    ) {
        db.movieDao().getMovieList()
    }

}