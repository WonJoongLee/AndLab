package com.plcoding.weatherapp.data.location

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.plcoding.weatherapp.domain.location.LocationTracker
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultLocationTracker @Inject constructor(
    // gps를 추적하는데는 lcoationManager와 FusedLocationProviderClient이 있는데,
    // 구글에서는 배터리가 더 절약된다는 대표적인 이유로 FusedLocationProviderClient를 권장한다.
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
) : LocationTracker {
    override suspend fun getCurrentLocation(): Location? {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val locationManager =
            application.getSystemService(Application.LOCATION_SERVICE) as android.location.LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(GPS_PROVIDER)
        Log.e("@@@fine", "$hasAccessFineLocationPermission")
        Log.e("coarse", "$hasAccessCoarseLocationPermission")
        Log.e("@@@gpsEnabled", ".$isGpsEnabled")
        if ((!hasAccessFineLocationPermission && !hasAccessCoarseLocationPermission) || !isGpsEnabled) {
            Log.e("@@@return", "null")
            return null
        }
        // callback을 suspending coroutine으로 바꿀 수 있다.
        return suspendCancellableCoroutine { cont -> // cont는 continuation
//            Log.e("@@@last", ".${locationClient.lastLocation.result}")
            locationClient.lastLocation.apply {
                // 위에 task라고 뜨는데 이는 async형식이어서 바로 location을 반환하는 것이 아니다.
                if (isComplete) {
                    if (isSuccessful) {
                        Log.e("@@@result", "${result}")
                        cont.resume(result)
                    } else {
                        Log.e("@@@isSuccess", "null")
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    Log.e("@@@succeslistener", ".$it")
                    cont.resume(it)
                }
                addOnFailureListener {
                    Log.e("@@@failure", "null")
                    cont.resume(null)
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}