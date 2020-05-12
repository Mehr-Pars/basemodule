package mehrpars.mobile.sample

import mehrpars.mobile.basemodule.BaseApp
import mehrpars.mobile.basemodule.data.network.retrofit.RetrofitUtil

class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()

        RetrofitUtil.init(this, "http://moviesapi.ir/", "authToken", true)

    }

    override fun getCountlyServerUrl(): String? {
        return "http://5.63.8.226"
    }

    override fun getCountlyApiKey(): String? {
        return "your_api_key"
    }

    override fun getNetworkCheckUrl(): String? {
        return "moviesapi.ir"
    }
}