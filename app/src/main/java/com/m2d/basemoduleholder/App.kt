package com.m2d.basemoduleholder

import android.app.Application
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.m2d.basemodule.retrofit.RetrofitUtil

class App: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()


        RetrofitUtil.init("http://moviesapi.ir/",true)
    }
}