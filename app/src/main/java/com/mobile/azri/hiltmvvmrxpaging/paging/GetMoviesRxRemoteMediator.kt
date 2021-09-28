package com.mobile.azri.hiltmvvmrxpaging.paging

import android.annotation.SuppressLint
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import com.mobile.azri.hiltmvvmrxpaging.db.MovieDatabase
import com.mobile.azri.hiltmvvmrxpaging.model.Movies
import com.mobile.azri.hiltmvvmrxpaging.model.MoviesMapper
import com.mobile.azri.hiltmvvmrxpaging.network.ApiService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.InvalidObjectException
import java.util.*
import javax.inject.Inject


/*Note
* Same as RxPagingSource,
* RxRemoteMediator need us to override loadSingle and return Single stream.
* The different between RxPagingSource is that we need to keep track of the page and also
* insert the result from API into database. Let’s run through the code.*/

//Untuk handle paging pada Room atau local dengan cara map data response ke data local
@ActivityRetainedScoped
@OptIn(ExperimentalPagingApi::class)
class GetMoviesRxRemoteMediator @Inject constructor(
    private val service : ApiService,
    private val database : MovieDatabase,
    private val apiKey : String,
    private val mapper : MoviesMapper,
    private val locale : Locale
) : RxRemoteMediator<Int, Movies.Movie>(){


    @SuppressLint("NewApi")
    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, Movies.Movie>
    ): Single<MediatorResult> {
        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .map {
                when(it){
                    LoadType.REFRESH ->{
                        val remoteKeys = getRemoteKeysClosetToCurrentPosition(state)
                        remoteKeys?.nextKey?.minus(1) ?: 1
                    }
                    //PREPEND will be called if user scroll to near top
                    LoadType.PREPEND ->{
                        val remoteKeys = getRemoteKeyForFirstItem(state) ?: throw InvalidObjectException("Result is Empty")
                        remoteKeys?.prevKey ?: INVALID_PAGE
                    }
                    //APPEND will be called if user scroll to near bottom, depends on paging configuration.
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state) ?: throw InvalidObjectException("Result is Empty")
                        remoteKeys.nextKey ?: INVALID_PAGE
                    }
                }
            }.flatMap { page->
                if (page== INVALID_PAGE){
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                }else{
                    service.popularMovieRx(
                        apiKey = apiKey,
                        page = page,
                        language = locale.language)
                        .map { movieResponse->
                            movieResponse?.let {
                                    it1 -> mapper.transform(it1,locale)
                            }
                        }
                        .map {  insertToDb(page,loadType,it) }
                        .map<MediatorResult>{ MediatorResult.Success(endOfPaginationReached = true) }
                        .onErrorReturn { MediatorResult.Error(it) }
                }
            }
    }


    private fun getRemoteKeysClosetToCurrentPosition(state: PagingState<Int, Movies.Movie>) : Movies.MovieRemoteKeys?{
        return state.anchorPosition?.let { position->
            state.closestItemToPosition(position)?.movieId?.let { id ->
                database.movieRemoteKeysRxDao().remoteKeysByMovieId(id)
            }
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Movies.Movie>) : Movies.MovieRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty()}?.data?.firstOrNull()?.let { movie->
            database.movieRemoteKeysRxDao().remoteKeysByMovieId(movie.movieId)
        }
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, Movies.Movie>): Movies.MovieRemoteKeys?{
        return state.pages.lastOrNull {it.data.isEmpty()}?.data?.lastOrNull()?.let { movie->
            database.movieRemoteKeysRxDao().remoteKeysByMovieId(movie.movieId)
        }
    }

    @Suppress("DEPRECATION")
    private fun insertToDb(page:Int,loadType: LoadType,data:Movies) : Movies{
        database.beginTransaction()

        try {
            if (loadType == LoadType.REFRESH){
                database.movieRemoteKeysRxDao().clearRemoteKeys()
                database.moviesRxDao().clearMovies()
            }
            val prevKey = if (page==1) null else page - 1
            val nextKey = if (page==data.total) null else page + 1
            val keys = data.movies.map {
                Movies.MovieRemoteKeys(movieId = it.movieId,prevKey = prevKey,nextKey = nextKey)
            }

            database.movieRemoteKeysRxDao().insertAll(keys)
            database.moviesRxDao().insertAll(data.movies)
            database.setTransactionSuccessful()

        }finally {
            database.endTransaction()
        }
        return data
    }

    companion object{
        const val INVALID_PAGE = -1
    }
}