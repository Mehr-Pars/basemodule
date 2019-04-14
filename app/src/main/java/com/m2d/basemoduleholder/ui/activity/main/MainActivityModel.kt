package epeyk.mobile.module.basemoduleholder.ui.activity.main

import android.content.Context
import epeyk.mobile.module.basemodule.BaseModel
import epeyk.mobile.module.basemodule.retrofit.RetrofitUtil
import epeyk.mobile.module.basemoduleholder.ApiClient

class MainActivityModel(context: Context) : BaseModel(context) {

    private lateinit var client: ApiClient

    override fun initRetrofit() {
        client = RetrofitUtil.createService(ApiClient::class.java)
    }


//    fun muMethod(): Int {
//        return client.sampleMethod()
//    }

}