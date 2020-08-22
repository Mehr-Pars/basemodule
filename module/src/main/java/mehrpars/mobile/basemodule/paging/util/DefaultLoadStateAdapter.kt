package mehrpars.mobile.basemodule.paging.util

import android.util.Log
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter

/**
 * Created by Ali Arasteh
 */

/**
 * Adapter for displaying a RecyclerView item based on [LoadState], such as a loading spinner, or
 * a retry error button.
 * */
class DefaultLoadStateAdapter(private val adapter: PagingDataAdapter<*, *>) :
    LoadStateAdapter<NetworkStateItemViewHolder>() {

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
        if (loadState is LoadState.Error) {
            Log.e("LoadState", "~~~~Error: ${loadState.error.message}")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): NetworkStateItemViewHolder {
        return NetworkStateItemViewHolder(parent) { adapter.retry() }
    }
}