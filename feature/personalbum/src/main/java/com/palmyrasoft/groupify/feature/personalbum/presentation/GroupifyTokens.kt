package com.palmyrasoft.groupify.feature.personalbum.presentation

import androidx.compose.ui.graphics.Color

/**
 * Shared dark-theme tokens for the feature, mirroring the literals used by [PersonAlbumScreen]
 * so the new Groups screens read as the same app. Accent/background/card reuse the existing
 * app palette (not the prototype's #7C5CE6/#000); success/danger are new semantic colors the
 * design introduced for name validation.
 */
internal object GroupifyTokens {
    val Accent = Color(0xFF7B61FF)
    val AccentSoft = Color(0x247B61FF)        // ~14% accent — chip / selected-row fill
    val AccentBorder = Color(0x807B61FF)      // ~50% accent — outlined controls
    val Background = Color(0xFF0E0E0E)
    val Card = Color(0xFF1C1C1E)
    val CardElevated = Color(0xFF232326)
    val Field = Color(0xFF2C2C2E)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF9E9E9E)
    val TextDim = Color(0xFF5A5A5F)
    val Hairline = Color(0x14FFFFFF)          // ~8% white
    val Success = Color(0xFF34C759)
    val Danger = Color(0xFFFF6B6B)            // reuse existing app error red
    val Scrim = Color(0x8C000000)
}
