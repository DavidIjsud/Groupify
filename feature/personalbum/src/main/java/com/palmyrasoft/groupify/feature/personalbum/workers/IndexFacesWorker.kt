// feature/personalbum/src/main/.../workers/IndexFacesWorker.kt
package com.palmyrasoft.groupify.feature.personalbum.workers

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.palmyrasoft.groupify.feature.personalbum.data.prefs.IndexingOnboardingPrefs
import com.palmyrasoft.groupify.feature.personalbum.domain.usecase.IndexFacesAndEmbeddingsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Long-running worker that indexes all unindexed photos via [IndexFacesAndEmbeddingsUseCase].
 *
 * Runs as a foreground service (so the OS does not kill it while the app is in the background)
 * and reports per-photo progress through both:
 *  - The WorkManager progress API ([setProgress]) — consumed by [PersonAlbumViewModel].
 *  - A foreground notification updated every photo — visible in the notification shade.
 *
 * Enqueue it with the unique name [WORK_NAME] and [ExistingWorkPolicy.KEEP] so concurrent
 * taps do not spawn duplicate workers.
 */
@HiltWorker
class IndexFacesWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
    private val indexFacesAndEmbeddingsUseCase: IndexFacesAndEmbeddingsUseCase,
    private val notificationHelper: IndexingNotificationHelper,
    private val indexingPrefs: IndexingOnboardingPrefs,
) : CoroutineWorker(appContext, params) {

    /**
     * Called by WorkManager on API 31+ before [doWork] to obtain the ForegroundInfo
     * before the worker even starts, in case the system needs to show it immediately.
     */
    override suspend fun getForegroundInfo(): ForegroundInfo =
        buildForegroundInfo(current = 0, total = 0)

    override suspend fun doWork(): Result {
        // Immediately promote to a foreground service so Android won't kill us
        // while the app is in the background.
        setForeground(buildForegroundInfo(current = 0, total = 0))

        return try {
            indexFacesAndEmbeddingsUseCase().collect { progress ->
                // 1. Report structured progress to WorkManager observers (ViewModel).
                setProgress(
                    workDataOf(
                        KEY_PROCESSED to progress.current,
                        KEY_TOTAL to progress.total,
                    )
                )

                // 2. Keep the foreground notification text in sync so the user can see
                //    progress even when the app is not in the foreground.
                setForeground(buildForegroundInfo(progress.current, progress.total))
            }

            // All photos processed — persist the completion flag so future app launches
            // and MediaStore changes are allowed to trigger auto-indexing.
            indexingPrefs.markInitialIndexComplete()
            notificationHelper.showCompletionNotification()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    /**
     * Builds the [ForegroundInfo] that promotes this worker to a foreground service.
     *
     * On API 29+ (Q) we pass [ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC] so the
     * service type matches the declaration in AndroidManifest.xml (required on API 34+).
     */
    private fun buildForegroundInfo(current: Int, total: Int): ForegroundInfo {
        val notification = notificationHelper.buildProgressNotification(current, total)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                IndexingNotificationHelper.PROGRESS_NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
            )
        } else {
            ForegroundInfo(
                IndexingNotificationHelper.PROGRESS_NOTIFICATION_ID,
                notification,
            )
        }
    }

    companion object {
        const val WORK_NAME = "face_indexing"
        const val KEY_PROCESSED = "processed"
        const val KEY_TOTAL = "total"

        /**
         * Enqueues a one-time indexing run with [ExistingWorkPolicy.KEEP], so a worker
         * that is already running or enqueued is never duplicated.
         *
         * Safe to call from [MediaStoreObserver], [GroupifyApp.onCreate], or any ViewModel.
         */
        fun enqueueOneTime(workManager: WorkManager) {
            val request = OneTimeWorkRequestBuilder<IndexFacesWorker>().build()
            workManager.enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.KEEP, request)
        }
    }
}
