package com.m2d.basemoduleholder.ui.activity.main

import android.content.Context
import com.m2d.basemodule.BaseModel
import com.m2d.basemodule.retrofit.RetrofitUtil
import com.m2d.basemoduleholder.ApiClient

class MainActivityModel(context: Context) : BaseModel(context) {

    private lateinit var client: ApiClient

    override fun initRetrofit() {
        client = RetrofitUtil.createService(ApiClient::class.java)
    }


//    fun muMethod(): Int {
//        return client.sampleMethod()
//    }

}