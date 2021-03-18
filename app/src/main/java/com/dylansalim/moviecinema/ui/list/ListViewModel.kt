package com.dylansalim.moviecinema.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylansalim.moviecinema.models.Movie
import com.dylansalim.moviecinema.models.Results
import com.dylansalim.moviecinema.network.MovieListSource
import com.dylansalim.moviecinema.utils.API_KEY
import com.dylansalim.moviecinema.utils.Event
import com.dylansalim.moviecinema.utils.ViewState
import com.dylansalim.moviecinema.utils.ViewStateEnum
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(private val networkSource: MovieListSource) : ViewModel() {

    //LiveData of selected movie, it stored the data of the movie selected in the home screen,
    private val _selectedMovie = MutableLiveData<Event<Movie>>()
    val selectedMovie: LiveData<Event<Movie>>
        get() = _selectedMovie

    //LiveData of movie results
    private var _movieResult = MutableLiveData<Event<Results>>()
    val movieResult: LiveData<Event<Results>>
        get() = _movieResult

    //LiveData of movie list
    private var _movieList = MutableLiveData<List<Movie>>()
    val movie: LiveData<List<Movie>>
        get() = _movieList

    // LiveData of List View State (LOADING/SUCCESS/FAILED)
    private var _listViewState = MutableLiveData<ViewState>()
    val listViewLoading: LiveData<ViewState>
        get() = _listViewState

    private val DEFAULT_PAGE = 1
    private val DEFAULT_LANGUAGE = "en-US"

    fun errorClickListener() {
        getMovieList(DEFAULT_PAGE)
    }

    fun clearMovieList(){
        _movieList.value = listOf()
    }

    fun getMovieList(page: Int, sortBy: String? = null) {
        viewModelScope.launch {
            try {
                _listViewState.value = ViewState(ViewStateEnum.LOADING.value)

                val movieResult: Results = networkSource.fetchMovieList(
                    API_KEY,
                    DEFAULT_LANGUAGE,
                    page,
                    sortBy
                )

                _movieResult.value = Event(movieResult)
                _listViewState.value = ViewState(ViewStateEnum.SUCCESS.value)
                _movieList.value = movie.value.orEmpty().plus(movieResult.networkMovie)
            } catch (e: Exception) {
                if (movieResult.value?.getContentIfNotHandled()?.networkMovie.isNullOrEmpty()) {
                    _listViewState.value = ViewState(ViewStateEnum.FAILED.value)
                }
            }
        }
    }

    fun displayPropertyDetails(movie: Movie) {
        _selectedMovie.value = Event(movie)
    }
}