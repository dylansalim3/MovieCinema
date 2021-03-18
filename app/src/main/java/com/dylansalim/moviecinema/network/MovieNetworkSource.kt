package com.dylansalim.moviecinema.network

import com.dylansalim.moviecinema.models.MovieInfo
import com.dylansalim.moviecinema.models.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class MovieListSource @Inject constructor(private val api: MovieApi) {
    suspend fun fetchMovieList(
        key: String, language: String, page: Int, sortBy: String? = null
    ): Results = withContext(Dispatchers.IO) {
        val movieListResult = api.getMovieListByPage(key, language, page, sortBy)
        movieListResult
    }
}

open class MovieDetailSource @Inject constructor(private val api: MovieApi) {
    suspend fun fetchDetailInformationOfMovie(id: Int): MovieInfo = withContext(Dispatchers.IO) {
        val movieDetail = api.getMovieByID(id)
        movieDetail
    }
}

//fun fetch(){
//    Observable.subscribeOn(()=>{
//
//    })
//}