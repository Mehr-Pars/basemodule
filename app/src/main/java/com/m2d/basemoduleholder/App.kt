package epeyk.mobile.module.basemoduleholder

import android.app.Application
import android.os.Build
import androidx.multidex.MultiDexApplication
import epeyk.mobile.module.basemodule.retrofit.RetrofitUtil

class App: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()


        RetrofitUtil.init("http://moviesapi.ir/",true)
    }
}