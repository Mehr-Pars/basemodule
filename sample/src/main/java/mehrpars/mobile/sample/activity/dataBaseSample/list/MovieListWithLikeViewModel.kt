package mehrpars.mobile.sample.activity.dataBaseSample.list

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import mehrpars.mobile.basemodule.ui.BaseViewModel
import mehrpars.mobile.sample.api.MovieListModel
import mehrpars.mobile.sample.db.model.MovieLikeModelDB

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

    fun likeMovie(movieid: Int) {

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