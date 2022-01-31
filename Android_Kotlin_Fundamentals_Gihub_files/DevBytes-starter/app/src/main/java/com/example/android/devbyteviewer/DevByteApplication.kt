/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.devbyteviewer

import android.app.Application
import android.os.Build
import androidx.work.*
import com.example.android.devbyteviewer.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 */
class DevByteApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)



    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    // onCreate에서 작업되는 것은 main thread에서 작동하기 때문에 오래 걸리는 작업은 main thread에서 하는 것을 피하는 것이 좋다
    private fun delayedInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED) // wifi 연결을 요구한다는 것
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                // 디바이스가 IDLE한 상태일 때만 실행하도록 하기
                // 사용자가 디바이스를 사용하지 않을 때 요청하도록 한다
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setRequiresDeviceIdle(true)
            }
            .build()
        // work request
        val repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS).setConstraints(constraints).build()
        Timber.d("Periodic Work request for sync is scheduled")
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // 같은 이름의 작업이 기존에 실행중일 때, 새로 들어온 작업을 실행하지 않고, 기존 작업을 계속 하도록 설정
            repeatingRequest
        )
    }


}
