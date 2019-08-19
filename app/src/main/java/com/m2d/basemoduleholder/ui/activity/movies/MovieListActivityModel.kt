package epeyk.mobile.module.basemoduleholder.ui.activity.movies

import android.content.Context
import epeyk.mobile.module.basemodule.ui.BaseModel
import epeyk.mobile.module.basemodule.data.network.retrofit.RetrofitUtil
import epeyk.mobile.module.basemoduleholder.ApiClient
import epeyk.mobile.module.basemoduleholder.model.api.MovieListModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MovieListActivityModel(context: Context) : BaseModel(context) {

    private lateinit var client: ApiClient

    override fun initRetrofit() {
        client = RetrofitUtil.createService(ApiClient::class.java)
    }

    fun getMovieList(page: Int): Observable<MovieListModel> {
        return client.getMovieList(page).subscribeOn(Schedulers.io())
    }
}