package com.palmyrasoft.groupify.feature.personalbum.workers

import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import androidx.work.WorkManager
import com.palmyrasoft.groupify.feature.personalbum.data.prefs.IndexingOnboardingPrefs
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Observes [android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI] for any change
 * (photo added, deleted, or modified) and enqueues [IndexFacesWorker] to index the delta.
 *
 * A 3-second debounce window collapses rapid bursts — such as camera burst mode or a
 * bulk import from another app — into a single indexing run instead of hammering the
 * worker queue with one enqueue per frame.
 *
 * Register this observer in Application.onCreate() so it stays alive for the full
 * lifetime of the app process (foreground and background).
 */
@Singleton
class MediaStoreObserver @Inject constructor(
    private val workManager: WorkManager,
    private val indexingPrefs: IndexingOnboardingPrefs,
) : ContentObserver(Handler(Looper.getMainLooper())) {

    // Re-use the same Handler for debouncing so there is only one pending callback at a time.
    private val handler = Handler(Looper.getMainLooper())
    private val enqueueWork = Runnable {
        // Only react to photo changes after the first index has been built.
        // Before that, the user hasn't opted in yet, so we must not start indexing silently.
        if (indexingPrefs.hasCompletedInitialIndex()) {
            IndexFacesWorker.enqueueOneTime(workManager)
        }
    }

    override fun onChange(selfChange: Boolean) {
        // Cancel any pending trigger and restart the debounce window.
        handler.removeCallbacks(enqueueWork)
        handler.postDelayed(enqueueWork, DEBOUNCE_MS)
    }

    companion object {
        /** Collapses rapid MediaStore bursts (camera burst, bulk import) into one run. */
        private const val DEBOUNCE_MS = 3_000L
    }
}
