package com.mobile.azri.hiltmvvmrxpaging.network

import com.mobile.azri.hiltmvvmrxpaging.model.MoviesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    fun popularMovieRx(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ) : Single<MoviesResponse?>

}