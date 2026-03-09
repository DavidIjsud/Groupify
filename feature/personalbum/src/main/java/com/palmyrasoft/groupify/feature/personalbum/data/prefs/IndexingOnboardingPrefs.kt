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

    fun hasSeenOnboarding(): Boolean = prefs.getBoolean(KEY_SEEN, false)

    fun markSeen() = prefs.edit().putBoolean(KEY_SEEN, true).apply()

    private companion object {
        const val KEY_SEEN = "indexing_onboarding_seen"
    }
}
