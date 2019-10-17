package epeyk.mobile.module.basemoduleholder

import epeyk.mobile.module.basemodule.BaseApp
import epeyk.mobile.module.basemodule.data.network.retrofit.RetrofitUtil

class App: BaseApp() {

    override fun onCreate() {
        super.onCreate()


        RetrofitUtil.init(this,"http://moviesapi.ir/", "authToken", true)
    }
}