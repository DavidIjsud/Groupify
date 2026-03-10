// app/src/main/java/com/palmyrasoft/groupify/GroupifyApp.kt
package com.palmyrasoft.groupify

import android.app.Application
import android.provider.MediaStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.palmyrasoft.groupify.feature.personalbum.data.prefs.IndexingOnboardingPrefs
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

    @Inject
    lateinit var indexingPrefs: IndexingOnboardingPrefs

    override fun onCreate() {
        super.onCreate() // Hilt injection runs here — @Inject fields are ready after this line.

        // Only auto-index on launch after the user has completed the first indexing run.
        // On first install this is skipped — the user must explicitly start the search flow,
        // acknowledge the onboarding dialog, and let indexing complete before we ever run it
        // automatically on future launches.
        if (indexingPrefs.hasCompletedInitialIndex()) {
            IndexFacesWorker.enqueueOneTime(workManager)
        }

        // Watch for new photos added while the app is alive (foreground or background).
        // The observer itself is also gated on hasCompletedInitialIndex so it won't fire
        // before the first index exists.
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
