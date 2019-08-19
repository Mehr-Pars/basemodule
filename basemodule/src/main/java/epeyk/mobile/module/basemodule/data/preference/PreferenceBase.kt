package epeyk.mobile.module.basemodule.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Created by bootiyar on 8/7/2019 - 10:18 AM.
 * Project Name: storeb2b.
 */
open class PreferenceBase(protected var context: Context, protected var name: String) {

    private var mPrefs: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    protected fun addParams(key: String, value: String) {
        mPrefs.edit {
            putString(key, value)
        }
    }

    protected fun getParams(key: String) = mPrefs.getString(key, "") ?: ""
}