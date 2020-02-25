package mehrpars.mobile.basemodule.data.model

import com.google.gson.annotations.SerializedName

open class ErrorResponse(
        @JvmSuppressWildcards
        @SerializedName("error")
        var error: String = "",
        @JvmSuppressWildcards
        @SerializedName("error_description")
        var errorDescription: String = ""
) {

    open fun hasError() = errorDescription.isNotEmpty()
}