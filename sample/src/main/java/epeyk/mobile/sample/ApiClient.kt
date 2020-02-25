package epeyk.mobile.sample

import io.reactivex.Observable
import epeyk.mobile.sample.api.MovieDetailModel
import epeyk.mobile.sample.api.MovieListModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiClient {


    @GET("/api/v1/movies?")
    fun getMovieList(
        @Query("page") page: Int
    ): Observable<MovieListModel>

    @GET("/api/v1/movies/{id}")
    fun getMovieDetail(
        @Path("id") movieId: Int
    ): Observable<MovieDetailModel>


}