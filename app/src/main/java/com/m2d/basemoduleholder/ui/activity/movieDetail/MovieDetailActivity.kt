package com.m2d.basemoduleholder.ui.activity.movieDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.m2d.basemodule.BaseActivity
import com.m2d.basemoduleholder.R

import com.m2d.basemoduleholder.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : BaseActivity<MovieDetailActivityViewModel>() {

    lateinit var binding:ActivityMovieDetailBinding
    var movieId=0

    override fun getBundle() {
        movieId=intent.getIntExtra("id",1)
    }

    override fun initBinding() {
        binding=DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        binding.viewmodel=viewModel
    }

    override fun initViewModel() {
        viewModel=ViewModelProviders.of(this).get(MovieDetailActivityViewModel::class.java)
        viewModel?.getMovieDetail(movieId)
    }

    override fun initViews() {

    }

    override fun initAdapter() {

    }

    override fun observeViewModelChange(viewModel: MovieDetailActivityViewModel?) {
        super.observeViewModelChange(viewModel)

        viewModel?.errorInLoadingData?.observe(this, Observer {
            Toast.makeText(this,"can not load data",Toast.LENGTH_SHORT).show()
            finish()
        })
    }
}
