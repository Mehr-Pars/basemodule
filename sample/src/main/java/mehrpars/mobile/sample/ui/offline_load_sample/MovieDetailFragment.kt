package mehrpars.mobile.sample.ui.offline_load_sample

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import mehrpars.mobile.basemodule.data.Result
import mehrpars.mobile.basemodule.isNetworkError
import mehrpars.mobile.basemodule.ui.BaseFragment
import mehrpars.mobile.sample.R
import mehrpars.mobile.sample.databinding.FragmentMovieDetailBinding

class MovieDetailFragment :
    BaseFragment<MovieDetailViewModel, FragmentMovieDetailBinding>(R.layout.fragment_movie_detail) {
    private val TAG = "MovieDetailFragment"


    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
    }

    override fun bindView(binding: FragmentMovieDetailBinding) {
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorGreen)
        binding.swipeRefresh.setOnRefreshListener { viewModel?.movieDetail?.refresh() }
    }

    override fun observeViewModelChange(viewModel: MovieDetailViewModel?) {
        super.observeViewModelChange(viewModel)

        viewModel?.movieDetail?.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.LOADING -> {
                    if (!binding.swipeRefresh.isRefreshing)
                        binding.swipeRefresh.isRefreshing = true
                }

                Result.Status.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.movie = result?.data
                }

                Result.Status.ERROR -> {
                    binding.swipeRefresh.isRefreshing = false
                    result.error?.let { showError(it) }
                    Log.e(TAG, "result error: ${result.message}")
                }
            }
        })

    }

    private fun showError(error: Throwable) {
        val snackBar = when {
            error.isNetworkError() -> {
                Snackbar.make(
                    binding.swipeRefresh,
                    "Network Connection Error",
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.retry) { viewModel?.movieDetail?.refresh() }
            }
            else -> {
                Snackbar.make(
                    binding.swipeRefresh,
                    error.message ?: "error occurred",
                    Snackbar.LENGTH_LONG
                )
            }
        }

        snackBar.setBackgroundTint(resources.getColor(R.color.colorRed))
            .show()
    }

}