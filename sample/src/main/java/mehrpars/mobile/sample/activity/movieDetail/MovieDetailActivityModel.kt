package mehrpars.mobile.sample.activity.movieDetail

import android.content.Context
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import mehrpars.mobile.basemodule.data.network.retrofit.RetrofitUtil
import mehrpars.mobile.basemodule.ui.BaseModel
import mehrpars.mobile.sample.ApiClient
import mehrpars.mobile.sample.api.MovieDetailModel

class MovieDetailActivityModel(context: Context) : BaseModel(context) {

    lateinit var client: ApiClient

    override fun initRetrofit() {
        client = RetrofitUtil.createService(ApiClient::class.java)
    }


    fun getMovieDetail(movieId: String): Observable<MovieDetailModel> {
        return client.getMovieDetail(movieId).subscribeOn(Schedulers.io())
    }
}