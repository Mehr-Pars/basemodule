package mehrpars.mobile.sample.activity.target

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_target.*
import mehrpars.mobile.basemodule.ui.BaseActivity
import mehrpars.mobile.sample.R
import mehrpars.mobile.sample.adapter.MyAdapter
import mehrpars.mobile.sample.adapter.model.DataModel
import mehrpars.mobile.sample.databinding.ActivityTargetBinding

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

        initAdapter()
    }

    private fun initAdapter() {
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
