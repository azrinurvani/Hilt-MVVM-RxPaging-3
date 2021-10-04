package com.mobile.azri.hiltmvvmrxpaging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.rxjava3.cachedIn
import com.mobile.azri.hiltmvvmrxpaging.model.Movies
import com.mobile.azri.hiltmvvmrxpaging.repository.GetMoviesRxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

@HiltViewModel
class GetMoviesRxViewModel @Inject constructor(
    private val repository: GetMoviesRxRepository
) : ViewModel(){

    fun getFavouriteMovies() : Flowable<PagingData<Movies.Movie>>{
        return repository.getMovies()
            .map { pagingData -> pagingData.filter { it.poster != null } }
            .cachedIn(viewModelScope)
    }
}