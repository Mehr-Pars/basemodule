package mehrpars.mobile.sample.ui.offline_load_sample

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import mehrpars.mobile.basemodule.data.Result
import mehrpars.mobile.basemodule.data.error.DefaultError
import mehrpars.mobile.basemodule.data.error.GeneralError
import mehrpars.mobile.basemodule.data.error.NetworkError
import mehrpars.mobile.basemodule.ui.BaseFragment
import mehrpars.mobile.sample.R
import mehrpars.mobile.sample.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : BaseFragment<MovieDetailViewModel, FragmentMovieDetailBinding>() {

    private val TAG = "MovieDetailFragment"

    override fun onCreateViewModel(): MovieDetailViewModel {

        return ViewModelProvider(this).get(MovieDetailViewModel::class.java)

    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailBinding {

        return FragmentMovieDetailBinding.inflate(inflater, container, false)

    }

    override fun bindView(binding: FragmentMovieDetailBinding) {

        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorGreen)
        binding.swipeRefresh.setOnRefreshListener { viewModel?.movieDetail?.refresh() }

    }

    override fun observeViewModelChange(viewModel: MovieDetailViewModel) {
        super.observeViewModelChange(viewModel)

        viewModel.movieDetail?.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.LOADING -> {
                    if (binding?.swipeRefresh?.isRefreshing == false)
                        binding!!.swipeRefresh.isRefreshing = true
                }

                Result.Status.SUCCESS -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    binding?.movie = result?.data
                }

                Result.Status.ERROR -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    Log.e(TAG, "result error: ${result.message}")
                }
            }
        })

    }

    override fun handleError(error: GeneralError) {
        val snackBar = when (error) {
            is NetworkError -> {
                Snackbar.make(
                    binding!!.swipeRefresh,
                    "Network Connection Error",
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.retry) { viewModel?.movieDetail?.refresh() }
            }
            is DefaultError -> {
                Snackbar.make(
                    binding!!.swipeRefresh,
                    error.error.message ?: "error occurred",
                    Snackbar.LENGTH_LONG
                )
            }
            else -> null
        }

        snackBar?.setBackgroundTint(resources.getColor(R.color.colorRed))?.show()
    }

}