package com.m2d.basemoduleholder.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.m2d.basemoduleholder.BR
import com.m2d.basemoduleholder.R
import com.m2d.basemoduleholder.model.api.MovieListModel
import com.m2d.basemoduleholder.ui.activity.movies.ItemMovieListViewModel

class AdapterMovieList2: BaseQuickAdapter<MovieListModel.Data, AdapterMovieList2.ViewHolder>(R.layout.item_movie_list_activity) {
    lateinit var viewmodel:ItemMovieListViewModel

    override fun convert(helper: ViewHolder?, item: MovieListModel.Data?) {
        val binding = helper?.binding
        viewmodel=ItemMovieListViewModel(mContext,item!!)
        binding?.setVariable(BR.viewmodel,viewmodel)
        binding?.executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false)
            ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {

        val binding: ViewDataBinding
            get() = itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }
}