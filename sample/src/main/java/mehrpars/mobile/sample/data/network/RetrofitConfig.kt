package mehrpars.mobile.sample.data.network

import mehrpars.mobile.basemodule.data.network.RetrofitConfig
import mehrpars.mobile.sample.AppConfig

object RetrofitConfig : RetrofitConfig() {

    override fun isLogEnabled(): Boolean {
        return true
    }

    override fun getBaseUrl(): String {
        return AppConfig.apiUrl!!
    }

//    override fun getHeaders(): List<Pair<String, String>>? {
//        return listOf(Pair("Content-Type", "application/json"))
//    }
}