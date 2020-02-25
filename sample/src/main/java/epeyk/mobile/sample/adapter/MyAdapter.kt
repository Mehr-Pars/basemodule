package epeyk.mobile.sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import epeyk.mobile.sample.R
import epeyk.mobile.sample.activity.target.ItemTargetViewModel
import epeyk.mobile.sample.adapter.model.DataModel
import epeyk.mobile.sample.databinding.ItemTargetActivityBinding


class MyAdapter(private val context: Context) : RecyclerView.Adapter<MyAdapter.DataViewHolder>() {

    private val TAG = "DataAdapter"
    private var data: MutableList<DataModel> = mutableListOf()


    fun setNewData(data: MutableList<DataModel>) {
        this.data = data
    }

    fun addNewData(data: MutableList<DataModel>) {
        this.data.addAll(data)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_target_activity,
            FrameLayout(parent.context), false
        )
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val dataModel = data.get(position)
        holder.setViewModel(ItemTargetViewModel(context, dataModel))
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onViewAttachedToWindow(holder: DataViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.bind()
    }

    override fun onViewDetachedFromWindow(holder: DataViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unbind()
    }

    fun updateData(data: MutableList<DataModel>) {
        if (data.isEmpty()) {
            this.data.clear()
        } else {
            this.data.addAll(data)
        }
        notifyDataSetChanged()
    }

    class DataViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var binding: ItemTargetActivityBinding? = null

        init {
            bind()
        }

        fun bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView)
            }
        }

        fun unbind() {
            if (binding != null) {
                binding!!.unbind() // Don't forget to unbind
            }
        }

        fun setViewModel(viewModel: ItemTargetViewModel) {
            if (binding != null) {
                binding!!.viewmodel = viewModel
            }
        }
    }

}