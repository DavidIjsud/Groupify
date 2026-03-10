package com.palmyrasoft.groupify.feature.personalbum.data.prefs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndexingOnboardingPrefs @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences("groupify_prefs", Context.MODE_PRIVATE)

    /** True once the user has read the first-time indexing explanation dialog. */
    fun hasSeenOnboarding(): Boolean = prefs.getBoolean(KEY_ONBOARDING_SEEN, false)
    fun markOnboardingSeen() = prefs.edit().putBoolean(KEY_ONBOARDING_SEEN, true).apply()

    /**
     * True once at least one full indexing run has completed successfully.
     * Gates auto-indexing on launch and MediaStore-triggered indexing so the app
     * never starts indexing without the user explicitly triggering it on first install.
     */
    fun hasCompletedInitialIndex(): Boolean = prefs.getBoolean(KEY_INITIAL_INDEX_DONE, false)
    fun markInitialIndexComplete() = prefs.edit().putBoolean(KEY_INITIAL_INDEX_DONE, true).apply()

    private companion object {
        const val KEY_ONBOARDING_SEEN = "indexing_onboarding_seen"
        const val KEY_INITIAL_INDEX_DONE = "initial_index_completed"
    }
}
