package com.m2d.basemoduleholder.ui.activity.dataBaseSample.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.m2d.basemodule.BaseViewModel
import com.m2d.basemoduleholder.db.model.MovieLikeModelDB
import com.m2d.basemoduleholder.model.api.MovieListModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.ResourceSingleObserver

class MovieListWithLikeViewModel(application: Application) : BaseViewModel(application) {

    var model = MovieListWithLikeModel(application)
    var movieData = MutableLiveData<MovieListModel>()

    fun getMovieList(page: Int) {
        compositeDisposable.add(
            model.getMovieList(page).observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                object : DisposableObserver<MovieListModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: MovieListModel) {
                        movieData.value = t
                    }

                    override fun onError(e: Throwable) {

                    }
                }
            )
        )
    }

    fun likeMovie(movieid:Int) {

        model.likeMovieInDB(MovieLikeModelDB(movieId = movieid))
    }

    fun unlikeMovie(movieId: Int) {
        compositeDisposable.add(
            model.getLikedMovie(movieId).observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                object : DisposableObserver<MovieLikeModelDB>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: MovieLikeModelDB) {
                        model.unlikeMovieInDB(t)
                    }

                    override fun onError(e: Throwable) {
                        //movie not found
                    }
                })
        )

    }


}