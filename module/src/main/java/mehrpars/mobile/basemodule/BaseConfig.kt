package mehrpars.mobile.basemodule

open class BaseConfig {
    open var countlyApiKey = "countly_api_key"
    open var apiUrl: String? = ""
    open var baseUrl: String? = ""
    open var networkCheckUrl: String? = "www.google.com"
    open var dataBaseName: String = "AppDataBase"

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