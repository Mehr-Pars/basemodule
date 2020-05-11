package mehrpars.mobile.sample.activity.main

import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import mehrpars.mobile.basemodule.ui.BaseActivity
import mehrpars.mobile.sample.R
import mehrpars.mobile.sample.activity.dataBaseSample.list.MovieListWithLike
import mehrpars.mobile.sample.activity.movies.MovieListActivity
import mehrpars.mobile.sample.activity.target.TargetActivity
import mehrpars.mobile.sample.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainActivityViewModel>() {

    lateinit var binding: ActivityMainBinding

    override fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = viewModel
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    override fun initViews() {
        viewModel?.getLink()

    }


    override fun observeViewModelChange(viewModel: MainActivityViewModel?) {
        super.observeViewModelChange(viewModel)

        viewModel?.gotoRecyclerSampleActivity?.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, TargetActivity::class.java))
            }
        })

        viewModel?.gotoMovies?.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, MovieListActivity::class.java))
            }
        })

        viewModel?.gotoMoviesDB?.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, MovieListWithLike::class.java))
            }
        })
    }
}
