package mehrpars.mobile.sample.data.network

import mehrpars.mobile.sample.data.model.MovieDetailResponse
import mehrpars.mobile.sample.data.model.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("top_rated_movies")
    suspend fun getMovies(@Query("page") page: Int): Response<MovieListResponse>
//    @GET("imdb_top_250/page={page}")
//    suspend fun getMovies(@Path("page") page: Int): MovieListResponse

    @GET("movie_detail")
    suspend fun getMovieDetail(@Query("movie_id") movieId: String): Response<MovieDetailResponse>
//    @GET("imadb_movie_detail/movie_id={id}")
//    suspend fun getMovieDetail(@Path("id") movieId: String): Response<MovieDetailResponse>

}