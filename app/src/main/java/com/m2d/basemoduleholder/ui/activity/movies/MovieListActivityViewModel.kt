package epeyk.mobile.module.basemoduleholder.ui.activity.movies

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import epeyk.mobile.module.basemodule.BaseViewModel
import epeyk.mobile.module.basemoduleholder.getRateInPersian
import epeyk.mobile.module.basemoduleholder.model.api.MovieListModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver

class MovieListActivityViewModel(application: Application) : BaseViewModel(application) {
    override fun initViews() {

    }

    override fun initAdapter() {

    }

    var model = MovieListActivityModel(context)
    var movieData = MutableLiveData<MovieListModel>()

    fun getMovieList(page: Int) {

        compositeDisposable.add(
            model.getMovieList(page).observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                object : DisposableObserver<MovieListModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: MovieListModel) {
                        Log.v("masood", "page : $page - size of this page : " + t.data.size)
                        movieData.value = t

                    }

                    override fun onError(e: Throwable) {
                        Log.v("masood", "error : " + e.message)
                    }
                })
        )
    }


}

