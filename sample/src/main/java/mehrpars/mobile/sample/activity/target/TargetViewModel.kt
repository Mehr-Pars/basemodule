package mehrpars.mobile.sample.activity.target

import android.app.Application
import mehrpars.mobile.basemodule.ui.BaseViewModel
import mehrpars.mobile.sample.adapter.MyAdapter

class TargetViewModel(application: Application) : BaseViewModel(application) {

    lateinit var adapter: MyAdapter

}