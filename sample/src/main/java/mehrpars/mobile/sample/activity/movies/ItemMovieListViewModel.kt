package mehrpars.mobile.sample.activity.movies


import android.content.Context
import androidx.databinding.ObservableField
import mehrpars.mobile.sample.api.MovieListModel


class ItemMovieListViewModel(application: Context, private val model: MovieListModel.Data) {

    protected val context by lazy { application }

    var cover = ObservableField<String>("")
    var name = ObservableField<String>("")


    init {
        cover.set(model.poster)
        name.set(model.title)
    }

    fun onClickItem() {
        //do something
    }
}