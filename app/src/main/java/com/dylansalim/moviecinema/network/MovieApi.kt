package com.dylansalim.moviecinema.network

import com.dylansalim.moviecinema.models.MovieInfo
import com.dylansalim.moviecinema.models.Results
import com.dylansalim.moviecinema.utils.API_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("discover/movie")
    suspend fun getMovieListByPage(
        @Query("api_key") key: String = API_KEY,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String? = null
    ): Results


    @GET("movie/{category}")
    suspend fun getMovieListByPageSorted(
        @Path("category") category: String,
        @Query("api_key") key: String = API_KEY,
        @Query("language") language: String,
        @Query("sort_by") sortBy: Int
    ): Results


    @GET("movie/{id}?api_key=$API_KEY&language=en")
    suspend fun getMovieByID(@Path("id") id: Int): MovieInfo
}