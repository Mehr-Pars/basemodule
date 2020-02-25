package epeyk.mobile.sample.activity.main

import android.content.Context
import epeyk.mobile.basemodule.data.network.retrofit.RetrofitUtil
import epeyk.mobile.basemodule.ui.BaseModel
import epeyk.mobile.sample.ApiClient

class MainActivityModel(context: Context) : BaseModel(context) {

    private lateinit var client: ApiClient

    override fun initRetrofit() {
        client = RetrofitUtil.createService(ApiClient::class.java)
    }


//    fun muMethod(): Int {
//        return client.sampleMethod()
//    }

}