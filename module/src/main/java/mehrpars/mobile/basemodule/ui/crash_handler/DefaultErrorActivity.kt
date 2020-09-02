/*
 * Copyright 2015 Eduard Ereza Martínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mehrpars.mobile.basemodule.ui.crash_handler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_default_error_handler.*
import mehrpars.mobile.basemodule.R
import mehrpars.mobile.basemodule.isDebuggable
import mehrpars.mobile.debugtools.ui.activity.error_activity.CustomActivityOnCrash

/**
 * این اکتیویتی در زمان کرش کردن برنامه توسط کلاس CustomActivityOnCrash
 * فراخوانی میشود و بجای بیرون رفتن از برناهم کاربر را به صفحه اصلی برنامه هدایت میکند
 */
class DefaultErrorActivity : AppCompatActivity() {

    init {
//        LocaleUtils.updateConfig(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_error_handler)

        val restartActivityClass = CustomActivityOnCrash.getRestartActivityClassFromIntent(intent)
        val eventListener = CustomActivityOnCrash.getEventListenerFromIntent(intent)

        if (restartActivityClass != null) {
            restartButton!!.setOnClickListener {
                val intent =
                    Intent(this@DefaultErrorActivity, CustomActivityOnCrash.restartActivityClass)
                CustomActivityOnCrash.restartApplicationWithIntent(
                    this@DefaultErrorActivity, intent, eventListener
                )
            }
        } else {
            restartButton!!.setOnClickListener {
                CustomActivityOnCrash.closeApplication(
                    this@DefaultErrorActivity,
                    eventListener
                )
            }
        }

        // show error
        if (isDebuggable()) {
            errorText.isVisible = true
            errorText.text = CustomActivityOnCrash.getAllErrorDetailsFromIntent(
                this@DefaultErrorActivity, intent
            )
        } else {
            errorText.isVisible = false
        }
    }

    override fun onBackPressed() {
        if (restartButton != null)
            restartButton!!.performClick()
        else
            super.onBackPressed()
    }
}
