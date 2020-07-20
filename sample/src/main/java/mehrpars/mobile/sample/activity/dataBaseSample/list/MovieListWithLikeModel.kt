package mehrpars.mobile.sample.activity.dataBaseSample.list

import android.content.Context
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import mehrpars.mobile.basemodule.data.network.retrofit.RetrofitUtil
import mehrpars.mobile.basemodule.ui.BaseModel
import mehrpars.mobile.sample.ApiClient
import mehrpars.mobile.sample.api.MovieListModel
import mehrpars.mobile.sample.db.MovieLikedDatabase
import mehrpars.mobile.sample.db.model.MovieLikeModelDB


class MovieListWithLikeModel(context: Context) : BaseModel(context) {

    lateinit var client: ApiClient
    lateinit var database: MovieLikedDatabase

    override fun initRetrofit() {
        client = RetrofitUtil.createService(ApiClient::class.java)
        database = MovieLikedDatabase.getInstance(context)!!
    }

    private fun getMovieListFromServer(page: Int): Observable<MovieListModel> {
        return client.getMovieList(page).subscribeOn(Schedulers.io())
    }

    private fun getMovieLikedListFromDB(): Observable<List<MovieLikeModelDB>> {
        return database.movieLikeDao().getAllLikedMovie().subscribeOn(Schedulers.io())
    }

    fun getMovieList(page: Int): Observable<MovieListModel> {
        return Observable.combineLatest(getMovieListFromServer(page), getMovieLikedListFromDB(),
            object : BiFunction<MovieListModel, List<MovieLikeModelDB>, MovieListModel> {
                override fun apply(
                    t1: MovieListModel,
                    t2: List<MovieLikeModelDB>
                ): MovieListModel {
                    for (i in 0 until t1.data.size) {
                        for (j in 0 until t2.size) {
                            if (t1.data[i].id == t2[j].movieId) {
                                t1.data[i].liked = true
                                break
                            }
                        }
                    }
                    return t1
                }
            })
    }

//    fun updateLikesInDB(movies:List<MovieLikeModelDB>) {
//      Single.fromCallable {
//            database.movieLikeDao().insertLikedMovie(movies)
//        }.subscribeOn(Schedulers.io()).subscribe()
//    }

    fun likeMovieInDB(movie: MovieLikeModelDB) {
        Single.fromCallable {
            database.movieLikeDao().insertLikedMovie(movie)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun unlikeMovieInDB(movie: MovieLikeModelDB) {
        Single.fromCallable {
            database.movieLikeDao().unlikeMovie(movie)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun getLikedMovie(movieId: Int): Observable<MovieLikeModelDB> {
        return database.movieLikeDao().getLikedMovieById(movieId).subscribeOn(Schedulers.io())
    }


}