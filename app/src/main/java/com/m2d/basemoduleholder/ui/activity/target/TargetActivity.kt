package epeyk.mobile.module.basemoduleholder.ui.activity.target

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import epeyk.mobile.module.basemodule.ui.BaseActivity
import epeyk.mobile.module.basemoduleholder.R
import epeyk.mobile.module.basemoduleholder.databinding.ActivityTargetBinding
import epeyk.mobile.module.basemoduleholder.adapter.MyAdapter
import epeyk.mobile.module.basemoduleholder.adapter.model.DataModel
import kotlinx.android.synthetic.main.activity_target.*

class TargetActivity : BaseActivity<TargetViewModel>() {

    lateinit var binding:ActivityTargetBinding
    lateinit var adapter: MyAdapter


    override fun getBundle() {

    }

    override fun initBinding() {
        binding=DataBindingUtil.setContentView(this,R.layout.activity_target)
        binding.viewmodel=viewModel
    }

    override fun initViewModel() {
        viewModel=ViewModelProviders.of(this).get(TargetViewModel::class.java)
    }

    override fun initViews() {
        recyclerTarget.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }

    override fun initAdapter() {
        adapter= MyAdapter(this).apply {
            setNewData(makeAdapterData())
        }
        viewModel?.adapter=adapter
    }

    override fun observeViewModelChange(viewModel: TargetViewModel?) {
        super.observeViewModelChange(viewModel)
    }

    fun makeAdapterData(): MutableList<DataModel> {
        var list= mutableListOf<DataModel>()

        for(i in 0 until 20)
        {
            list.add(
                DataModel(
                    "title -> " + (i + 1),
                    "https://picsum.photos/200/200/?image=" + (541 + i)
                )
            )
        }

        return list
    }
}
