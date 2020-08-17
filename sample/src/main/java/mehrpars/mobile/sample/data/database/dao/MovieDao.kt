package mehrpars.mobile.sample.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import mehrpars.mobile.basemodule.database.dao.BaseDao
import mehrpars.mobile.sample.data.model.entity.Movie

/**
 * The Data Access Object for the Movie class.
 */
@Dao
interface MovieDao : BaseDao<Movie> {

    @Query("SELECT * FROM movie ORDER BY 'order' DESC")
    fun getMovieList(): PagingSource<Int, Movie>

    @Query("DELETE FROM movie")
    suspend fun deleteAll()

}
