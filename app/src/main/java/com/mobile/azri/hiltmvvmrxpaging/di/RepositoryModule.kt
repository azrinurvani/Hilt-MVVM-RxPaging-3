package com.mobile.azri.hiltmvvmrxpaging.di

import com.mobile.azri.hiltmvvmrxpaging.db.MovieDatabase
import com.mobile.azri.hiltmvvmrxpaging.model.MoviesMapper
import com.mobile.azri.hiltmvvmrxpaging.network.ApiService
import com.mobile.azri.hiltmvvmrxpaging.paging.GetMoviesRxRemoteMediator
import com.mobile.azri.hiltmvvmrxpaging.repository.GetMoviesRxRemoteRepositoryImpl
import com.mobile.azri.hiltmvvmrxpaging.repository.GetMoviesRxRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @ActivityRetainedScoped
    @Provides
    fun provideMoviesMapper() : MoviesMapper{
        return MoviesMapper()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideRxRemoteMediator(service: ApiService, database: MovieDatabase, apikey:String, mapper: MoviesMapper, locale: Locale): GetMoviesRxRemoteMediator{
        return GetMoviesRxRemoteMediator(service,database,apikey,mapper,locale)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetMoviesRepository(database: MovieDatabase,rxRemoteMediator: GetMoviesRxRemoteMediator) : GetMoviesRxRepository {
        return GetMoviesRxRemoteRepositoryImpl(database,rxRemoteMediator)
    }
}