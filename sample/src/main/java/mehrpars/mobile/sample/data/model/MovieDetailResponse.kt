package mehrpars.mobile.sample.data.model

import com.google.gson.annotations.SerializedName
import mehrpars.mobile.sample.data.model.entity.Movie

open class MovieDetailResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("error_message")
    var errorMessage: String,
    @SerializedName("result")
    var movie: Movie
)