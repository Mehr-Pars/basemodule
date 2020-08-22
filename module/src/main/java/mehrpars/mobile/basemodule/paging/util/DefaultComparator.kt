package mehrpars.mobile.basemodule.paging.util

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Ali Arasteh
 */

/**
 *  [DefaultComparator] is default comparator used in [PagingDataAdapter]
 *  for comparing objects
 * */
class DefaultComparator<T : Comparable> : DiffUtil.ItemCallback<T>() {
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.contentEqualsTo(newItem)

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.objectEqualsTo(newItem)
}