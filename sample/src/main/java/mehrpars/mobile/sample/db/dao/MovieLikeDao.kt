package mehrpars.mobile.sample.db.dao

import androidx.room.*
import io.reactivex.Observable
import mehrpars.mobile.sample.db.model.MovieLikeModelDB

@Dao
interface MovieLikeDao {

    @Query("SELECT * FROM MOVIELIKEMODELDB WHERE movieId=:mId")
    fun getLikedMovieById(mId: Int): Observable<MovieLikeModelDB>

    @Query("SELECT * FROM MovieLikeModelDB")
    fun getAllLikedMovie(): Observable<List<MovieLikeModelDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLikedMovie(movie: MovieLikeModelDB)

    @Delete
    fun unlikeMovie(movie: MovieLikeModelDB)


}