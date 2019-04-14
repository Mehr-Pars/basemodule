package com.m2d.basemoduleholder.ui.activity.movieDetail

import android.content.Context
import com.m2d.basemodule.BaseModel
import com.m2d.basemodule.retrofit.RetrofitUtil
import com.m2d.basemoduleholder.ApiClient
import com.m2d.basemoduleholder.model.api.MovieDetailModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MovieDetailActivityModel(context: Context):BaseModel(context) {

    lateinit var client: ApiClient

    override fun initRetrofit() {
        client=RetrofitUtil.createService(ApiClient::class.java)
    }


    fun getMovieDetail(movieId:Int): Observable<MovieDetailModel> {
        return client.getMovieDetail(movieId).subscribeOn(Schedulers.io())
    }
}