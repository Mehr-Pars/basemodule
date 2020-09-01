package mehrpars.mobile.sample.ui.offline_load_sample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import mehrpars.mobile.basemodule.data.Result
import mehrpars.mobile.basemodule.ui.BaseFragment
import mehrpars.mobile.sample.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : BaseFragment<MovieDetailViewModel>() {
    private val TAG = "MovieDetailFragment"
    lateinit var binding: FragmentMovieDetailBinding

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
    }

    override fun initViewAndBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initLayoutView() {

    }

    override fun observeViewModelChange(viewModel: MovieDetailViewModel?) {
        super.observeViewModelChange(viewModel)

        viewModel?.movieDetail?.observe(viewLifecycleOwner, Observer { result ->

            when (result.status) {
                Result.Status.LOADING -> {
                    Log.i(TAG, "loading: $result")
                }

                Result.Status.SUCCESS -> {
                    Log.i(TAG, "success: $result")
                    result.data?.let { movieDetail ->
                        binding.movie = movieDetail
                    }

                }

                Result.Status.ERROR -> {
                    Log.e(TAG, result.message)
                }
            }

        })
    }

}