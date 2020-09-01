package mehrpars.mobile.sample

import mehrpars.mobile.basemodule.BaseConfig

/**
 * Created by Ali Arasteh
 */

object AppConfig : BaseConfig() {

    init {
        // using mocking server
        apiUrl = "https://e6614244-6bfe-4910-87d4-1679412909c4.mock.pstmn.io"
        networkCheckUrl = "e6614244-6bfe-4910-87d4-1679412909c4.mock.pstmn.io"
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