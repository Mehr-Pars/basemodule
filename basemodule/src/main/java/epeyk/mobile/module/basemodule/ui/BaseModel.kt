package epeyk.mobile.module.basemodule.ui

import android.content.Context

abstract class BaseModel(val context: Context) {

    init {
        getInstance()
    }

    private fun getInstance() {
        initRetrofit()
    }

    open fun initRetrofit() {}

}