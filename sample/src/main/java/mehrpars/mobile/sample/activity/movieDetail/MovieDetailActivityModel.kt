package mehrpars.mobile.sample.activity.movieDetail

import android.content.Context
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import mehrpars.mobile.basemodule.data.network.retrofit.RetrofitUtil
import mehrpars.mobile.basemodule.ui.BaseModel
import mehrpars.mobile.sample.ApiClient
import mehrpars.mobile.sample.api.MovieDetailModel

class MovieDetailActivityModel(context: Context) : BaseModel(context) {

    lateinit var client: ApiClient

    override fun initRetrofit() {
        client = RetrofitUtil.createService(ApiClient::class.java)
    }


    fun getMovieDetail(movieId: Int): Observable<MovieDetailModel> {
        return client.getMovieDetail(movieId).subscribeOn(Schedulers.io())
    }
}