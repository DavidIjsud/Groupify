// feature/personalbum/src/main/.../domain/recognition/TextQueryEmbedder.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.recognition

/**
 * Embeds a natural-language query (e.g. "red dog on a bed") into the same CLIP vector space as
 * [ImageEmbedder], so the query can be cosine-matched against indexed photo embeddings. Returns an
 * L2-normalized vector.
 */
interface TextQueryEmbedder {
    suspend fun embedQuery(text: String): FloatArray
}
