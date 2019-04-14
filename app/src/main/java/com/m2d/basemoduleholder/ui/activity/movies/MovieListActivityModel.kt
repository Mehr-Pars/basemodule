package com.m2d.basemoduleholder.ui.activity.movies

import android.content.Context
import com.m2d.basemodule.BaseModel
import com.m2d.basemodule.retrofit.RetrofitUtil
import com.m2d.basemoduleholder.ApiClient
import com.m2d.basemoduleholder.model.api.MovieListModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MovieListActivityModel(context: Context):BaseModel(context) {

    private lateinit var client:ApiClient

    override fun initRetrofit() {
        client=RetrofitUtil.createService(ApiClient::class.java)
    }


    fun getMovieList(page:Int): Observable<MovieListModel> {
        return client.getMovieList(page).subscribeOn(Schedulers.io())
    }
}