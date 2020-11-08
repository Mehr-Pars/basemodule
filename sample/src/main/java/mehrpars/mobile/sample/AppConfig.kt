package mehrpars.mobile.sample

import mehrpars.mobile.basemodule.BaseConfig

/**
 * Created by Ali Arasteh
 */

object AppConfig : BaseConfig() {

    init {
        // using mock server
        apiUrl = "https://cc70afb9-d3bd-4570-889a-a8d3b1c96e7c.mock.pstmn.io"
        networkCheckUrl = "cc70afb9-d3bd-4570-889a-a8d3b1c96e7c.mock.pstmn.io"
//        apiUrl = "https://mgit.mparsict.com/public-android/fakedata/-/raw/master/"
//        networkCheckUrl = "mgit.mparsict.com"
        dataBaseName = "BaseModuleSample.sqlite"
    }

    fun getCompleteUrl(path: String?): String {
        return when {
            path == null -> "-"
            path.isEmpty() -> "-"
            path.startsWith("http") -> path
            path.startsWith("/") -> baseUrl + path.substring(1)
            else -> baseUrl + path
        }
    }
}