package epeyk.mobile.sample

import epeyk.mobile.basemodule.BaseApp
import epeyk.mobile.basemodule.data.network.retrofit.RetrofitUtil
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