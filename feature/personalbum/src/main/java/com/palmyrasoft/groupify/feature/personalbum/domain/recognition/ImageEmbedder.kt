// feature/personalbum/src/main/.../domain/recognition/ImageEmbedder.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.recognition

/**
 * Embeds a whole image into a CLIP-style semantic vector so photos can be ranked against a typed
 * description. Sibling of [FaceEmbedder] (which embeds a single cropped face); here we embed the
 * entire photo. Returns an L2-normalized vector so cosine similarity equals the dot product.
 */
interface ImageEmbedder {
    suspend fun embedImage(photoUri: String): FloatArray
}
