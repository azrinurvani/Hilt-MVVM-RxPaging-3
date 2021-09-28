package com.mobile.azri.hiltmvvmrxpaging.repository

import androidx.paging.PagingData
import com.mobile.azri.hiltmvvmrxpaging.model.Movies
import io.reactivex.rxjava3.core.Flowable

interface GetMoviesRxRepository {

    fun getMovies() : Flowable<PagingData<Movies.Movie>>
}