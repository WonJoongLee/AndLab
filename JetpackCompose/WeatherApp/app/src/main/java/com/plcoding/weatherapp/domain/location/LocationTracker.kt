package com.plcoding.weatherapp.domain.location

// 원래는 domain에 android가 있는 것이 좋지는 않지만, 편의를 위해 여기다 둔다.
// kmm과 같이 정말 pure해야 한다면 빠져야 한다.
import android.location.Location

interface LocationTracker {
    // location permission이 없는 경우, 현재 gps를 가져올 수 없는 경우 대비
    suspend fun getCurrentLocation(): Location?
}
