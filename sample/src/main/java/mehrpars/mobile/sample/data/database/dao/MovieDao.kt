package mehrpars.mobile.sample.data.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mehrpars.mobile.basemodule.data.database.dao.BaseDao
import mehrpars.mobile.sample.data.model.entity.Movie

/**
 * The Data Access Object for the Movie class.
 */
@Dao
interface MovieDao : BaseDao<Movie> {

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getMovieDetail(movieId: String): LiveData<Movie>

    @Query("SELECT * FROM movie ORDER BY 'order' DESC")
    fun getMovieList(): PagingSource<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveAll(objectList: List<Movie>)

    @Query("DELETE FROM movie")
    suspend fun deleteAll()

}
