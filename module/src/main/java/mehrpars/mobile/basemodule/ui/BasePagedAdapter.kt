package mehrpars.mobile.basemodule.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import mehrpars.mobile.basemodule.paging.util.Comparable
import mehrpars.mobile.basemodule.paging.util.DefaultComparator

open class BaseViewHolder<B : ViewDataBinding>(val binding: B) :
    RecyclerView.ViewHolder(binding.root)

abstract class BasePagedAdapter<T : Comparable, VB : ViewDataBinding>(
    comparator: DiffUtil.ItemCallback<T>? = null, private val layoutId: Int
) : PagingDataAdapter<T, BaseViewHolder<VB>>(comparator ?: DefaultComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binding = DataBindingUtil.inflate<VB>(
            LayoutInflater.from(parent.context), layoutId, parent, false
        )
        return BaseViewHolder(binding)
    }

    @CallSuper
    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        val item = getItem(position)
        onBindView(holder.binding, item, position)
    }

    abstract fun onBindView(binding: VB, item: T?, position: Int)
}