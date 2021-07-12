package mehrpars.mobile.basemodule.data.error

class NetworkError : GeneralError {
    companion object {
        fun instance() = NetworkError()
    }
}