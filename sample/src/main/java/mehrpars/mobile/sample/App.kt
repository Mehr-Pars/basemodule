package mehrpars.mobile.sample

import mehrpars.mobile.basemodule.BaseApp
import mehrpars.mobile.basemodule.data.network.retrofit.RetrofitUtil
import java.util.*

class App : BaseApp() {
    companion object {
        const val COUNTLY_SERVER_URL = "http://5.63.8.226"

        // countly api key for SAMT
        const val COUNTLY_API_KEY = "your_api_key"
    }

    init {
        appLocale = Locale("en")
    }

    override fun onCreate() {
        super.onCreate()

        RetrofitUtil.init(this, "http://moviesapi.ir/", "authToken", true)

        // setup countly for crash reporting
        initCountly(COUNTLY_SERVER_URL, COUNTLY_API_KEY)
    }
}