package epeyk.mobile.module.basemoduleholder.ui.activity.dataBaseSample.list


import android.content.Context
import androidx.databinding.ObservableField
import epeyk.mobile.module.basemoduleholder.model.api.MovieListModel


class ItemMovieListWithLikeViewModel(private val model: MovieListModel.Data) {

    var cover = ObservableField<String>("")
    var name = ObservableField<String>("")
    var liked=ObservableField<Boolean>()

    init {
        cover.set(model.poster)
        name.set(model.title)
        liked.set(model.liked)
    }
}