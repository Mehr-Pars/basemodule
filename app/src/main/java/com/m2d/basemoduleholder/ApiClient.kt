package epeyk.mobile.module.basemoduleholder

import epeyk.mobile.module.basemoduleholder.model.api.MovieDetailModel
import epeyk.mobile.module.basemoduleholder.model.api.MovieListModel
import epeyk.mobile.module.basemoduleholder.ui.activity.movies.MovieListActivityModel
import io.reactivex.Observable
import retrofit2.http.*


interface ApiClient {



    @GET("/api/v1/movies?")
    fun getMovieList(
        @Query("page") page: Int
    ):Observable<MovieListModel>

    @GET("/api/v1/movies/{id}")
    fun getMovieDetail(
        @Path("id") movieId: Int
    ):Observable<MovieDetailModel>


}