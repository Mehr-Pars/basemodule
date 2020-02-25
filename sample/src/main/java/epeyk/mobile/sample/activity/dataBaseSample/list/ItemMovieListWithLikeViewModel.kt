package epeyk.mobile.sample.activity.dataBaseSample.list


import androidx.databinding.ObservableField
import epeyk.mobile.sample.api.MovieListModel


class ItemMovieListWithLikeViewModel(private val model: MovieListModel.Data) {

    var cover = ObservableField<String>("")
    var name = ObservableField<String>("")
    var liked = ObservableField<Boolean>()

    init {
        cover.set(model.poster)
        name.set(model.title)
        liked.set(model.liked)
    }
}