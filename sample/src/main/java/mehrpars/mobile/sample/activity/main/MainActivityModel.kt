package mehrpars.mobile.sample.activity.main

import android.content.Context
import mehrpars.mobile.basemodule.data.network.retrofit.RetrofitUtil
import mehrpars.mobile.basemodule.ui.BaseModel
import mehrpars.mobile.sample.ApiClient

class MainActivityModel(context: Context) : BaseModel(context) {

    private lateinit var client: ApiClient

    override fun initRetrofit() {
        client = RetrofitUtil.createService(ApiClient::class.java)
    }


//    fun muMethod(): Int {
//        return client.sampleMethod()
//    }

}