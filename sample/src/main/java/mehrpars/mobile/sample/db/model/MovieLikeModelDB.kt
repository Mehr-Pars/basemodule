package mehrpars.mobile.sample.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieLikeModelDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val movieId: Int
)