package mehrpars.mobile.basemodule.paging.util

import androidx.recyclerview.widget.DiffUtil

class DefaultComparator<T : Comparable> : DiffUtil.ItemCallback<T>() {
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.contentEqualsTo(newItem)

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.objectEqualsTo(newItem)
}