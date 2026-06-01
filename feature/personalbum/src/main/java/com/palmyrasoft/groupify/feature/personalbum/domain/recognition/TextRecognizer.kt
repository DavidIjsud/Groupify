// feature/personalbum/src/main/.../domain/recognition/TextRecognizer.kt
package com.palmyrasoft.groupify.feature.personalbum.domain.recognition

/**
 * Extracts the visible text (OCR) from an image.
 *
 * Mirrors [com.palmyrasoft.groupify.feature.personalbum.domain.detection.FaceDetector]: a single
 * suspend call that takes a photo URI and returns the recognized text, with all heavy decoding /
 * inference done off the main thread by the implementation. Returns an empty string when no text
 * is found.
 */
interface TextRecognizer {
    suspend fun recognizeText(photoUri: String): String
}
