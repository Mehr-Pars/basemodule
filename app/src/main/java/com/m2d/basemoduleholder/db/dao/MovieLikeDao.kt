package com.m2d.basemoduleholder.db.dao

import androidx.room.*
import com.m2d.basemoduleholder.db.model.MovieLikeModelDB
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MovieLikeDao {

    @Query("SELECT * FROM MOVIELIKEMODELDB WHERE movieId=:mId")
    fun getLikedMovieById(mId:Int):Observable<MovieLikeModelDB>

    @Query("SELECT * FROM MovieLikeModelDB")
    fun getAllLikedMovie():Observable<List<MovieLikeModelDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLikedMovie(movie:MovieLikeModelDB)

    @Delete
    fun unlikeMovie(movie:MovieLikeModelDB)


}