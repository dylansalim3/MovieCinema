package com.dylansalim.moviecinema.utils

// Define the view state of the presenter (LOADING/SUCCESS/FAILED)
data class ViewState(
    val currentState: Int = 0
)

enum class ViewStateEnum(var value: Int) {
    LOADING(0), SUCCESS(1), FAILED(-1);
}
