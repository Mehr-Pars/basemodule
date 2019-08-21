package epeyk.mobile.module.basemoduleholder.ui.activity.target

import android.content.Context
import android.widget.Toast
import androidx.databinding.ObservableField
import epeyk.mobile.module.basemoduleholder.adapter.model.DataModel

class ItemTargetViewModel(application: Context,private val dataModel: DataModel) {

    protected val context by lazy { application }

    var iconLink = ObservableField<String>("")
    var title = ObservableField<String>("")


    init {
        iconLink.set(dataModel.iconLink)
        title.set(dataModel.title)
    }


    fun onCloseClick() {
        Toast.makeText(context, "close clicked", Toast.LENGTH_SHORT).show()
    }

    fun onDownloadClick() {
        Toast.makeText(context, "download clicked", Toast.LENGTH_SHORT).show()
    }
}