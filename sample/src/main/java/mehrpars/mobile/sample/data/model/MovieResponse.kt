package mehrpars.mobile.sample.data.model

import com.google.gson.annotations.SerializedName
import mehrpars.mobile.sample.data.model.entity.Movie

open class MovieResponse(
    @SerializedName("count")
    var count: Int,
    @SerializedName("current_page")
    var currentPage: Int,
    @SerializedName("total_results")
    var totalResults: Int,
    @SerializedName("results")
    var movieList: List<Movie>
)