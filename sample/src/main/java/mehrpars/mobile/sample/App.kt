package mehrpars.mobile.sample

import mehrpars.mobile.basemodule.BaseApp
import mehrpars.mobile.sample.data.network.RetrofitConfig


class App : BaseApp() {
    override fun getCountlyApiKey(): String? {
        return AppConfig.countlyApiKey
    }

    override fun getCountlyServerUrl(): String? {
        return AppConfig.countlyServerUrl
    }

    override fun getNetworkCheckUrl(): String? {
        return AppConfig.networkCheckUrl
    }

    override fun onCreate() {
        super.onCreate()

        RetrofitConfig.init(this)
    }
}