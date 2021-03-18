package com.dylansalim.moviecinema.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class OrderBottomSheetViewModel @Inject constructor() : ViewModel() {
    private var _selectOrder = MutableLiveData<Int>()
    val selectOrder: LiveData<Int>
        get() = _selectOrder


    fun setSelectOrder(order: Int) {
        _selectOrder.value = order
    }

}