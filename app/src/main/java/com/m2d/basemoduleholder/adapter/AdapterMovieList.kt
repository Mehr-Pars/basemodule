package com.m2d.basemoduleholder.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.DataBindingUtil
import android.content.Context
import android.widget.FrameLayout
import android.view.LayoutInflater
import android.view.ViewGroup
import com.m2d.basemoduleholder.R
import com.m2d.basemoduleholder.databinding.ItemMovieListActivityBinding
import com.m2d.basemoduleholder.model.api.MovieListModel
import com.m2d.basemoduleholder.ui.activity.movies.ItemMovieListViewModel


class AdapterMovieList(private val context: Context) : RecyclerView.Adapter<AdapterMovieList.DataViewHolder>() {

    private val TAG = "DataAdapter"
    private var data: MutableList<MovieListModel.Data> = mutableListOf()


    fun setNewData(data: MutableList<MovieListModel.Data>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun addNewData(data: MutableList<MovieListModel.Data>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun getData(): MutableList<MovieListModel.Data> {
        return this.data
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_movie_list_activity,
            FrameLayout(parent.context), false
        )
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val movieListModel = data.get(position)
        holder.setViewModel(ItemMovieListViewModel(context, movieListModel))
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

    fun updateData(data: MutableList<MovieListModel.Data>) {
        if (data.isEmpty()) {
            this.data.clear()
        } else {
            this.data.addAll(data)
        }
        notifyDataSetChanged()
    }

    class DataViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var binding: ItemMovieListActivityBinding? = null

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

        fun setViewModel(viewModel: ItemMovieListViewModel) {
            if (binding != null) {
                binding!!.viewmodel = viewModel
            }
        }
    }

}