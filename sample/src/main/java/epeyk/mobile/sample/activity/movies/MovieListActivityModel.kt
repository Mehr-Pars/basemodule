package epeyk.mobile.sample.activity.movies

import android.content.Context
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import epeyk.mobile.basemodule.data.network.retrofit.RetrofitUtil
import epeyk.mobile.basemodule.ui.BaseModel
import epeyk.mobile.sample.ApiClient
import epeyk.mobile.sample.api.MovieListModel

class MovieListActivityModel(context: Context) : BaseModel(context) {

    private lateinit var client: ApiClient

    override fun initRetrofit() {
        client = RetrofitUtil.createService(ApiClient::class.java)
    }

    fun getMovieList(page: Int): Observable<MovieListModel> {
        return client.getMovieList(page).subscribeOn(Schedulers.io())
    }
}