package epeyk.mobile.module.basemodule

import android.content.Context

abstract class BaseModel(val context: Context) {

    init {
        getInstance()
    }


    private fun getInstance() {

        initRetrofit()

    }



    protected abstract fun initRetrofit()
}