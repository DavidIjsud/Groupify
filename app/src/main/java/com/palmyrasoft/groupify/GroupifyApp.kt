// app/src/main/java/com/palmyrasoft/groupify/GroupifyApp.kt
package com.palmyrasoft.groupify

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GroupifyApp : Application(), Configuration.Provider {

    /**
     * Injected by Hilt during Application.onCreate().
     * Used to give WorkManager a factory that can create @HiltWorker instances.
     *
     * The default WorkManagerInitializer is disabled in AndroidManifest.xml so WorkManager
     * only initializes on the first WorkManager.getInstance() call — by which time Hilt has
     * already injected this field.
     */
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
