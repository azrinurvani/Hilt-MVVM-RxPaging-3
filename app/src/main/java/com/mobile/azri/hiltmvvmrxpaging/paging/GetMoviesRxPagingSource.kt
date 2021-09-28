package com.mobile.azri.hiltmvvmrxpaging.paging

import android.annotation.SuppressLint
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.mobile.azri.hiltmvvmrxpaging.model.Movies
import com.mobile.azri.hiltmvvmrxpaging.model.MoviesMapper
import com.mobile.azri.hiltmvvmrxpaging.network.ApiService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@ActivityRetainedScoped
class GetMoviesRxPagingSource @Inject constructor(
    private val service : ApiService,
    private val apiKey : String,
    private val mapper : MoviesMapper,
    private val locale : Locale
) : RxPagingSource<Int, Movies.Movie>(){


    override fun getRefreshKey(state: PagingState<Int, Movies.Movie>): Int? {
        return null
    }

    @SuppressLint("NewApi")
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Movies.Movie>> {
        val position = params.key ?: 1

        return service.popularMovieRx(apiKey,position,locale.language)
            .subscribeOn(Schedulers.io())
            .map { it?.let { it1 -> mapper.transform(it1,locale) } } //map data from response to local
            .map { toLoadResult(it,position) } //map data from response to LoadResult.Page() for paging
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(data: Movies,position:Int): LoadResult<Int,Movies.Movie>{
        return LoadResult.Page(
            data = data.movies,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.total) null else position + 1
        )
    }

}