package com.mobile.azri.hiltmvvmrxpaging.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.rxjava3.cachedIn
import com.mobile.azri.hiltmvvmrxpaging.model.Movies
import com.mobile.azri.hiltmvvmrxpaging.repository.GetMoviesRxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class GetMoviesRxViewModel @Inject constructor(
    private val repository: GetMoviesRxRepository
) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()
    private var moviesLiveData_ : MutableLiveData<PagingData<Movies.Movie>> = MutableLiveData()
    val moviesLiveData get() =  moviesLiveData_

//    fun getFavouriteMovies() : Flowable<PagingData<Movies.Movie>>{
//        return repository.getMovies()
//            .map { pagingData -> pagingData.filter { it.poster != null } }
//            .cachedIn(viewModelScope)
//    }

    init {
        loadMovieLists()
    }

    @ExperimentalCoroutinesApi
    fun loadMovieLists(){
        compositeDisposable.add(
            repository.getMovies()
                .subscribeOn(Schedulers.io())
                .map { pagingData -> pagingData.filter {  it.poster != null } }
                .cachedIn(viewModelScope)
                .subscribe(
                    {
                        Log.d(TAG, "loadMovieLists: data -> $it")
                        moviesLiveData_.postValue(it)
                    },{
                        Log.d(TAG, "loadMovieLists: throwable -> ${it.localizedMessage}")
                        moviesLiveData_.postValue(null)
                    }
                )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun disposeComposite(){
        compositeDisposable.dispose()
    }
    
    companion object {
        private const val TAG = "GetMoviesRxViewModel"
    }
}