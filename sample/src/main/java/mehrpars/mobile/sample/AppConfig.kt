package mehrpars.mobile.sample

import mehrpars.mobile.basemodule.network.BaseConfig

/**
 * Created by Ali Arasteh
 */

object AppConfig : BaseConfig() {

    init {
        // using mocking server
        apiUrl = "https://112e1af1-2948-4ea1-b328-cc794458237a.mock.pstmn.io/"
        networkCheckUrl = "112e1af1-2948-4ea1-b328-cc794458237a.mock.pstmn.io"
        dataBaseName = "BaseModuleSample"
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