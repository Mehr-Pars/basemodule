package mehrpars.mobile.basemodule.ui

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Ali Vatanparast
 */

abstract class BaseAdapter<Type : RecyclerView.ViewHolder, Data> : RecyclerView.Adapter<Type>() {

    protected lateinit var mRecyclerView: RecyclerView

    abstract fun setData(data: List<Data>)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    abstract class BaseViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bindData(item: Any)
    }
}
