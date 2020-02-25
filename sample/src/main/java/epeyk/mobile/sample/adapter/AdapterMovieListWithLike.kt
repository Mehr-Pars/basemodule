package epeyk.mobile.sample.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import epeyk.mobile.sample.BR
import epeyk.mobile.sample.R

import epeyk.mobile.sample.api.MovieListModel
import epeyk.mobile.sample.activity.dataBaseSample.list.ItemMovieListWithLikeViewModel

class AdapterMovieListWithLike :
    BaseQuickAdapter<MovieListModel.Data, AdapterMovieListWithLike.ViewHolder>(
        R.layout.item_movie_list_activity_with_like
    ) {
    lateinit var viewmodel: ItemMovieListWithLikeViewModel

    override fun convert(helper: ViewHolder, item: MovieListModel.Data?) {

        helper.addOnClickListener(R.id.imageLike)

        val binding = helper.binding
        viewmodel = ItemMovieListWithLikeViewModel(item!!)
        binding.setVariable(BR.viewmodel, viewmodel)
        binding.executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false)
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