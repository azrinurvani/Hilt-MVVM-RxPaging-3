package com.mobile.azri.hiltmvvmrxpaging.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.mobile.azri.hiltmvvmrxpaging.db.MovieDatabase
import com.mobile.azri.hiltmvvmrxpaging.model.Movies
import com.mobile.azri.hiltmvvmrxpaging.paging.GetMoviesRxRemoteMediator
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetMoviesRxRemoteRepositoryImpl @Inject constructor(
    private val database: MovieDatabase,
    private val remoteMediator: GetMoviesRxRemoteMediator
    ) : GetMoviesRxRepository{

    @ExperimentalPagingApi
    override fun getMovies(): Flowable<PagingData<Movies.Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { database.moviesRxDao().selectAll() }
        ).flowable
    }

}