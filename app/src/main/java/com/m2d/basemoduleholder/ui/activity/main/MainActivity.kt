package epeyk.mobile.module.basemoduleholder.ui.activity.main

import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import epeyk.mobile.module.basemodule.ui.BaseActivity
import epeyk.mobile.module.basemoduleholder.R
import epeyk.mobile.module.basemoduleholder.databinding.ActivityMainBinding
import epeyk.mobile.module.basemoduleholder.ui.activity.dataBaseSample.list.MovieListWithLike
import epeyk.mobile.module.basemoduleholder.ui.activity.movies.MovieListActivity
import epeyk.mobile.module.basemoduleholder.ui.activity.target.TargetActivity

class MainActivity : BaseActivity<MainActivityViewModel>() {

    lateinit var binding :ActivityMainBinding


    override fun getBundle() {

    }

    override fun initBinding() {
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        binding.viewmodel = viewModel

    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }
    override fun initViews() {
        viewModel?.getLink()

    }

    override fun initAdapter() {

    }

    override fun observeViewModelChange(viewModel: MainActivityViewModel?) {
        super.observeViewModelChange(viewModel)

        viewModel?.gotoRecyclerSampleActivity?.observe(this, Observer {
            if(it)
            {
                startActivity(Intent(this, TargetActivity::class.java))
            }
        })

        viewModel?.gotoMovies?.observe(this, Observer {
            if(it)
            {
                startActivity(Intent(this,MovieListActivity::class.java))
            }
        })

        viewModel?.gotoMoviesDB?.observe(this, Observer {
            if(it)
            {
                startActivity(Intent(this,MovieListWithLike::class.java))
            }
        })
    }
}
