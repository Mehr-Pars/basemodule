package epeyk.mobile.module.basemoduleholder.ui.activity.movieDetail

import android.content.Context
import epeyk.mobile.module.basemodule.ui.BaseModel
import epeyk.mobile.module.basemodule.data.network.retrofit.RetrofitUtil
import epeyk.mobile.module.basemoduleholder.ApiClient
import epeyk.mobile.module.basemoduleholder.model.api.MovieDetailModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MovieDetailActivityModel(context: Context): BaseModel(context) {

    lateinit var client: ApiClient

    override fun initRetrofit() {
        client=RetrofitUtil.createService(ApiClient::class.java)
    }


    fun getMovieDetail(movieId:Int): Observable<MovieDetailModel> {
        return client.getMovieDetail(movieId).subscribeOn(Schedulers.io())
    }
}