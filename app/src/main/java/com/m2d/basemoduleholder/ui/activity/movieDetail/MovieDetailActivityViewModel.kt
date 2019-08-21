package epeyk.mobile.module.basemoduleholder.ui.activity.movieDetail

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import epeyk.mobile.module.basemodule.ui.BaseViewModel
import epeyk.mobile.module.basemoduleholder.getRateInPersian
import epeyk.mobile.module.basemoduleholder.model.api.MovieDetailModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver

class MovieDetailActivityViewModel(application: Application) : BaseViewModel(application) {
    override fun initViews() {

    }

    override fun initAdapter() {

    }

    var model = MovieDetailActivityModel(context)
    var movieDetail = ObservableField<MovieDetailModel>()
    var topImage = ObservableField<String>("")
    private var albumIndex = 0
    var album = mutableListOf<String>()

    var errorInLoadingData = MutableLiveData<Boolean>()


    fun getMovieDetail(movieId: Int) {
        compositeDisposable.add(
            model.getMovieDetail(movieId)
                .map {
                    topImage.set(it.poster)
                    it.rated = context.getRateInPersian(it.rated)
                    album.add(it.poster!!)
                    it.images.forEach {
                        album.add(it)
                    }
                    it
                }.observeOn(AndroidSchedulers.mainThread())
                .retry(5)
                .subscribeWith(
                    object : DisposableObserver<MovieDetailModel>() {
                        override fun onComplete() {

                        }

                        override fun onNext(t: MovieDetailModel) {
                            movieDetail.set(t)
                        }

                        override fun onError(e: Throwable) {
//                            Log.v("masood", "error : " + e.message)
                            errorInLoadingData.value = true
                        }
                    })
        )
    }

    fun nextImage() {
        albumIndex++
        if (albumIndex == album.size)
            albumIndex = 0
        topImage.set(album[albumIndex])
//        Log.v("masood","poster : "+album[albumIndex])
    }

    fun prevImage() {
        albumIndex--
        if (albumIndex < 0)
            albumIndex = album.size - 1
        topImage.set(album[albumIndex])
//        Log.v("masood","poster : "+album[albumIndex])
    }


}