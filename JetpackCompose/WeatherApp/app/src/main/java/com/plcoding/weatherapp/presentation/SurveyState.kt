package com.plcoding.weatherapp.presentation

import androidx.annotation.DrawableRes

data class SurveyState(
    val firstQuestion: String = "",
    val secondQuestion: String = "",
//    @DrawableRes val imageResId: Int = 0
    val isIconVisible: Boolean = true
)