package com.palmyrasoft.groupify.feature.personalbum.presentation.model

/**
 * A single result in the matches grid.
 *
 * [showScore] is true for face matches (cosine similarity → percentage badge) and false for text
 * matches, which have no meaningful similarity score.
 */
data class MatchUiModel(
    val uri: String,
    val scorePercent: Int = 0,
    val showScore: Boolean = true,
)
