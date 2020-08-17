package mehrpars.mobile.sample.data.network

import mehrpars.mobile.sample.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("top_rated_movies")
    suspend fun getMovies(@Query("page") page: Int): MovieResponse
}