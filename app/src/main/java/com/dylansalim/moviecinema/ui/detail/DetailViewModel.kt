package com.dylansalim.moviecinema.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylansalim.moviecinema.models.MovieInfo
import com.dylansalim.moviecinema.network.MovieDetailSource
import com.dylansalim.moviecinema.utils.ViewState
import com.dylansalim.moviecinema.utils.ViewStateEnum
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val movieDetailSource: MovieDetailSource) :
    ViewModel() {

    //LiveData of Movie Detail
    private var _movieDetail = MutableLiveData<MovieInfo>()
    val movieDetail: LiveData<MovieInfo>
        get() = _movieDetail

    // LiveData of List View State (LOADING/SUCCESS/FAILED)
    private var _detailViewState = MutableLiveData<ViewState>()
    val detailViewLoading: LiveData<ViewState>
        get() = _detailViewState

    fun getSelectedMovieById(id: Int) {
        viewModelScope.launch {
            try {
                _detailViewState.value = ViewState(ViewStateEnum.LOADING.value)
                _movieDetail.value = movieDetailSource.fetchDetailInformationOfMovie(id)
                _detailViewState.value = ViewState(ViewStateEnum.SUCCESS.value)
            } catch (e: Exception) {
                _detailViewState.value = ViewState(ViewStateEnum.FAILED.value)
            }
        }
    }


}