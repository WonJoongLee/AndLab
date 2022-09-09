package com.plcoding.weatherapp.data.remote

import com.squareup.moshi.Json

/**
 * isaac point : 외부에서 받아오는 값이다보니 Double, Int, String으로 받는데
 * mapper를 통해 더 읽기 쉽게 WeatherType과 같은 data class를 통해 mapping해준다.
 * DTO -> mapping -> Domain Model
 */
data class WeatherDataDto(
    val time: List<String>,
    @field:Json(name = "temperature_2m")
    val temperatures: List<Double>,
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>,
    @field:Json(name = "pressure_msl")
    val pressures: List<Double>,
    @field:Json(name = "windspeed_10m")
    val windSpeeds: List<Double>,
    @field:Json(name = "relativehumidity_2m")
    val humidities: List<Double>
)
