package com.mobile.azri.hiltmvvmrxpaging.di

import android.app.Application
import android.util.Log
import com.mobile.azri.hiltmvvmrxpaging.db.MovieDatabase
import com.mobile.azri.hiltmvvmrxpaging.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    //Untuk penggunaan depdency bisa dimasukan sebagai parameter
    // ataupun langsung memanggil method depedency nya (ex: pada provideRetrofitBuilder)
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient{

        val intercept = HttpLoggingInterceptor {
                message -> Log.d("AZ-APP", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(intercept)
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(provideHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }


    @Singleton
    @Provides
    fun provideDatabaseBuilder(@ApplicationContext application: Application) : MovieDatabase {
        return MovieDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun provideLocale() : Locale{
        return Locale.getDefault()
    }
}