package com.m2d.basemoduleholder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.m2d.basemoduleholder.db.dao.MovieLikeDao
import com.m2d.basemoduleholder.db.model.MovieLikeModelDB

@Database(
    entities = [MovieLikeModelDB::class],
    version = 1
)
abstract class MovieLikedDatabase : RoomDatabase() {

    abstract fun movieLikeDao():MovieLikeDao

    companion object {
        const val DB_NAME="MovieLikeDataBase.db"

        private var INSTANCE:MovieLikedDatabase?=null
        fun getInstance(context: Context):MovieLikedDatabase?
        {
            INSTANCE?.let {
                return INSTANCE
            }

            synchronized(MovieLikedDatabase::class){
                INSTANCE = create(context)
                return INSTANCE
            }
        }
        private fun create(context: Context):MovieLikedDatabase
        {
            return Room.databaseBuilder(context,MovieLikedDatabase::class.java, DB_NAME).build()
        }
    }
}