package com.m2d.basemoduleholder.ui.activity.target

import android.app.Application
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.m2d.basemodule.BaseViewModel
import com.m2d.basemoduleholder.adapter.MyAdapter

class TargetViewModel(application: Application):BaseViewModel(application) {


    lateinit var adapter: MyAdapter

}