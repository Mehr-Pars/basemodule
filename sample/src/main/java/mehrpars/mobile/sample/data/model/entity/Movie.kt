package mehrpars.mobile.sample.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import mehrpars.mobile.basemodule.paging.util.Comparable

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    @field:SerializedName("index")
    val id: String,
    val title: String,
    val image: String,
    val poster: String?,
    val year: String,
    val rate: String,
    val description: String?
) : Comparable {

    override fun objectEqualsTo(item: Comparable): Boolean {
        return if (item is Movie) item.id == id else false
    }

    override fun contentEqualsTo(item: Comparable): Boolean {
        return this@Movie == item
    }

    override fun toString() = title
}