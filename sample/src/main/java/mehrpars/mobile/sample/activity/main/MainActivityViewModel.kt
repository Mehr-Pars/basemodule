package mehrpars.mobile.sample.activity.main

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import mehrpars.mobile.basemodule.ui.BaseViewModel

class MainActivityViewModel(application: Application) : BaseViewModel(application) {

    var links = listOf<String>(
        "https://picsum.photos/400/250/?image=541",
        "https://picsum.photos/400/250/?image=542",
        "https://picsum.photos/400/250/?image=543",
        "https://picsum.photos/400/250/?image=544",
        "https://picsum.photos/400/250/?image=545",
        "https://picsum.photos/400/250/?image=546",
        "https://picsum.photos/400/250/?image=547",
        "https://picsum.photos/400/250/?image=548",
        "https://picsum.photos/400/250/?image=549",
        "https://picsum.photos/400/250/?image=550"
    )
    var currentIndex = 0

    var topImageUrl = ObservableField<String>("")
    var name = ObservableField<String>("")
    var lastName = ObservableField<String>("")
    var nameLastName = ObservableField<String>("")
    var gotoRecyclerSampleActivity = MutableLiveData<Boolean>()
    var gotoMovies = MutableLiveData<Boolean>()
    var gotoMoviesDB = MutableLiveData<Boolean>()

    override fun initAdapter() {
        super.initAdapter()
    }

    fun getLink() {
        topImageUrl.set(links[currentIndex])
    }

    fun loadAnotherImage() {
        currentIndex++
        topImageUrl.set(links[currentIndex])

        if (currentIndex == links.size - 1)
            currentIndex = 0
    }

    fun showNameAndLastName() {
        nameLastName.set(name.get() + " " + lastName.get())
    }

    fun showToast() {
        Toast.makeText(
            context,
            "sample toast : " + name.get() + " " + lastName.get(),
            Toast.LENGTH_SHORT
        ).show()
        gotoRecyclerSampleActivity.value = true
    }

    fun goToMovies() {
        gotoMovies.value = true
    }

    fun goToMoviesWithDB() {
        safeRequest {
            gotoMoviesDB.value = true
        }
    }
}