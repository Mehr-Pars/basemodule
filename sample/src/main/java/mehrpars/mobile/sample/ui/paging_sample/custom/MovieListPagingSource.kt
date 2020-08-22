package mehrpars.mobile.sample.ui.paging_sample.custom

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import mehrpars.mobile.sample.data.model.entity.Movie
import mehrpars.mobile.sample.data.network.ApiClient
import retrofit2.HttpException
import java.io.IOException


class MovieListPagingSource(private val apiClient: ApiClient) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val pageNumber = params.key ?: 1

            val response = apiClient.getMovies(pageNumber)

            Page(data = response.movieList, prevKey = null, nextKey = response.currentPage + 1)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
