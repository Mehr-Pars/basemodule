package mehrpars.mobile.sample

import android.app.Activity
import mehrpars.mobile.basemodule.BaseApp
import mehrpars.mobile.sample.data.network.RetrofitConfig
import mehrpars.mobile.sample.ui.MainActivity


class App : BaseApp() {

    override fun getNetworkCheckUrl(): String? {
        return AppConfig.networkCheckUrl
    }

    override fun getRestartActivity(): Class<out Activity> {
        return MainActivity::class.java
    }

    override fun onCreate() {
        super.onCreate()

        RetrofitConfig.init(this)
    }
}