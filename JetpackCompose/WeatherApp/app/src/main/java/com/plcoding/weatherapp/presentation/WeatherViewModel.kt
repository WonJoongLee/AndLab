package com.plcoding.weatherapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.data.location.DefaultLocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: DefaultLocationTracker
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    var sheetState by mutableStateOf(SurveyState())
        private set

    init {
        sheetState = sheetState.copy(
            firstQuestion = "Have a great day, this is first question."
        )
    }

    fun showSecondQuestion() {
        sheetState = sheetState.copy(
            firstQuestion = "Have a great day, this is first question.",
            secondQuestion = "Good morning, this is second question."
        )
    }

    fun changeVisibility() {
        sheetState = sheetState.copy(
            isIconVisible = !sheetState.isIconVisible
        )
    }

    fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                when (
                    val result = repository.getWeatherData(location.latitude, location.longitude)
                ) {
                    is Resource.Success -> {
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: kotlin.run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }
}