package epeyk.mobile.module.basemoduleholder

import androidx.multidex.MultiDexApplication
import epeyk.mobile.module.basemodule.data.network.retrofit.RetrofitUtil

class App: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()


        RetrofitUtil.init("http://moviesapi.ir/", "authToken", true)
    }
}