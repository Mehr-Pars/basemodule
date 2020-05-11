package mehrpars.mobile.sample.activity.movieDetail

import android.app.Application
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import mehrpars.mobile.basemodule.ui.BaseViewModel
import mehrpars.mobile.sample.api.MovieDetailModel
import mehrpars.mobile.sample.getRateInPersian

class MovieDetailActivityViewModel(application: Application) : BaseViewModel(application) {
    var model = MovieDetailActivityModel(context)
    var movieDetail = ObservableField<MovieDetailModel>()
    var topImage = ObservableField<String>("")
    private var albumIndex = 0
    var album = mutableListOf<String>()
    lateinit var movieId: String

    var errorInLoadingData = MutableLiveData<Boolean>()

    override fun handleIntent(intent: Intent) {
        movieId = intent.getStringExtra("id") ?: "1"
    }

    fun getMovieDetail() {
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