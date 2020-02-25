package mehrpars.mobile.sample

import mehrpars.mobile.basemodule.BaseApp
import mehrpars.mobile.basemodule.data.network.retrofit.RetrofitUtil
import java.util.*

class App : BaseApp() {

    init {
        appLocale = Locale("en")
    }

    override fun onCreate() {
        super.onCreate()

        RetrofitUtil.init(this, "http://moviesapi.ir/", "authToken", true)
    }
}