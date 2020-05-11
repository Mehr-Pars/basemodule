package mehrpars.mobile.sample.activity.movieDetail

import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import mehrpars.mobile.basemodule.ui.BaseActivity
import mehrpars.mobile.sample.R

import mehrpars.mobile.sample.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : BaseActivity<MovieDetailActivityViewModel>() {
    lateinit var binding: ActivityMovieDetailBinding

    override fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        binding.viewmodel = viewModel
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MovieDetailActivityViewModel::class.java)
        viewModel?.getMovieDetail()
    }

    override fun initViews() {

    }

    override fun observeViewModelChange(viewModel: MovieDetailActivityViewModel?) {
        super.observeViewModelChange(viewModel)

        viewModel?.errorInLoadingData?.observe(this, Observer {
            Toast.makeText(this, "can not load data", Toast.LENGTH_SHORT).show()
            finish()
        })
    }
}
