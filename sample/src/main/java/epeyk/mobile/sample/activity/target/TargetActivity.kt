package epeyk.mobile.sample.activity.target

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_target.*
import epeyk.mobile.basemodule.ui.BaseActivity
import epeyk.mobile.sample.R
import epeyk.mobile.sample.adapter.MyAdapter
import epeyk.mobile.sample.adapter.model.DataModel
import epeyk.mobile.sample.databinding.ActivityTargetBinding

class TargetActivity : BaseActivity<TargetViewModel>() {

    lateinit var binding: ActivityTargetBinding
    lateinit var adapter: MyAdapter


    override fun getBundle() {

    }

    override fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_target)
        binding.viewmodel = viewModel
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(TargetViewModel::class.java)
    }

    override fun initViews() {
        recyclerTarget.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun initAdapter() {
        adapter = MyAdapter(this).apply {
            setNewData(makeAdapterData())
        }
        viewModel?.adapter = adapter
    }

    override fun observeViewModelChange(viewModel: TargetViewModel?) {
        super.observeViewModelChange(viewModel)
    }

    fun makeAdapterData(): MutableList<DataModel> {
        var list = mutableListOf<DataModel>()

        for (i in 0 until 20) {
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
