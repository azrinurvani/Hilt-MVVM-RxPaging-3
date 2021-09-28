package com.mobile.azri.hiltmvvmrxpaging.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.azri.hiltmvvmrxpaging.model.Movies

@Dao
interface MovieRemoteKeysRxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<Movies.MovieRemoteKeys>)

    @Query("SELECT * FROM movie_remote_keys WHERE movieId = :movieId")
    fun remoteKeysByMovieId(movieId: Long): Movies.MovieRemoteKeys?

    @Query("DELETE FROM movie_remote_keys")
    fun clearRemoteKeys()
}