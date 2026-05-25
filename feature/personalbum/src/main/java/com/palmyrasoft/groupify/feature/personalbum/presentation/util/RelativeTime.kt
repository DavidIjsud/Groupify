package com.palmyrasoft.groupify.feature.personalbum.presentation.util

import android.text.format.DateUtils

/**
 * Localized relative time ("Today", "Yesterday", "3 days ago", …) for group "updated" labels.
 * Uses the platform formatter so it follows the device language (en/es) for free.
 */
fun relativeUpdatedLabel(timestampMillis: Long, now: Long = System.currentTimeMillis()): String =
    DateUtils.getRelativeTimeSpanString(
        timestampMillis,
        now,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE,
    ).toString()
