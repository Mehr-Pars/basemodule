package epeyk.mobile.module.basemoduleholder

import epeyk.mobile.module.basemodule.BaseApp
import epeyk.mobile.module.basemodule.data.network.retrofit.RetrofitUtil
import java.util.*

class App: BaseApp() {

    init {
        appLocale = Locale("en")
    }

    override fun onCreate() {
        super.onCreate()

        RetrofitUtil.init(this,"http://moviesapi.ir/", "authToken", true)
    }
}