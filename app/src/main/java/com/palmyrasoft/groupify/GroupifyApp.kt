// app/src/main/java/com/palmyrasoft/groupify/GroupifyApp.kt
package com.palmyrasoft.groupify

import android.app.Application
import android.provider.MediaStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.palmyrasoft.groupify.feature.personalbum.workers.IndexFacesWorker
import com.palmyrasoft.groupify.feature.personalbum.workers.MediaStoreObserver
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

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var mediaStoreObserver: MediaStoreObserver

    override fun onCreate() {
        super.onCreate() // Hilt injection runs here — @Inject fields are ready after this line.

        // Index any photos that were added while the app process was not running.
        // KEEP policy means this is a no-op if a worker is already enqueued or running.
        IndexFacesWorker.enqueueOneTime(workManager)

        // Watch for new photos added while the app is alive (foreground or background).
        // The observer debounces bursts and re-enqueues IndexFacesWorker as needed.
        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            /* notifyForDescendants = */ true,
            mediaStoreObserver,
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
