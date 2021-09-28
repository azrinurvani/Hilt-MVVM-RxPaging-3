package com.mobile.azri.hiltmvvmrxpaging.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.mobile.azri.hiltmvvmrxpaging.model.Movies
import com.mobile.azri.hiltmvvmrxpaging.paging.GetMoviesRxPagingSource
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetMoviesRxRepositoryImpl @Inject constructor(
    private val pagingSource: GetMoviesRxPagingSource
    ) : GetMoviesRxRepository {

    override fun getMovies(): Flowable<PagingData<Movies.Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = {pagingSource}
        ).flowable
    }
}