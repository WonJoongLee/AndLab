package com.plcoding.weatherapp.domain.weather

/**
 * @param weatherDataPerDay 그 날짜에 대한 날씨 데이터이고, Int는 날(월~일), WeatherData는 날씨 데이터
 */
data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?
)
