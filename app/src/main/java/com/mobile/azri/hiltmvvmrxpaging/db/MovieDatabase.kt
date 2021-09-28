package com.mobile.azri.hiltmvvmrxpaging.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobile.azri.hiltmvvmrxpaging.db.dao.MovieRemoteKeysRxDao
import com.mobile.azri.hiltmvvmrxpaging.db.dao.MovieRxDao
import com.mobile.azri.hiltmvvmrxpaging.model.Movies
import com.mobile.azri.hiltmvvmrxpaging.util.Converters

@Database(
    entities = [Movies.Movie::class, Movies.MovieRemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun moviesRxDao(): MovieRxDao
    abstract fun movieRemoteKeysRxDao(): MovieRemoteKeysRxDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: buildDatabase(
                            context
                        ).also {
                            INSTANCE = it
                        }
                }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                MovieDatabase::class.java, "TMDB.db")
                .build()
    }
}