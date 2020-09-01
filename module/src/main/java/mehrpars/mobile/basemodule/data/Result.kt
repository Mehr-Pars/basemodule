package mehrpars.mobile.basemodule.data

/**
 * A generic class that holds a value with its loading status.
 *
 * Result is usually created by the Repository classes where they return
 * `LiveData<Result<T>>` to pass back the latest data to the UI with its fetch status.
 */
data class Result<out T>(
    val status: Status, val data: T?, val message: String?, val error: Throwable?
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Result<T> {
            return Result(
                Status.SUCCESS,
                data,
                null,
                null
            )
        }

        fun <T> error(
            message: String? = null, error: Throwable? = null, data: T? = null
        ): Result<T> {
            return Result(
                Status.ERROR,
                data,
                message,
                error
            )
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(
                Status.LOADING,
                data,
                null,
                null
            )
        }
    }

}