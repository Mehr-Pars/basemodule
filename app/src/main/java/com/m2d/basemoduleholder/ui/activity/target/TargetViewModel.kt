package epeyk.mobile.module.basemoduleholder.ui.activity.target

import android.app.Application
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import epeyk.mobile.module.basemodule.BaseViewModel
import epeyk.mobile.module.basemoduleholder.adapter.MyAdapter

class TargetViewModel(application: Application):BaseViewModel(application) {


    lateinit var adapter: MyAdapter

}