// feature/personalbum/src/main/.../data/ml/BitmapDecodeUtils.kt
package com.example.groupify.feature.personalbum.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.example.groupify.feature.personalbum.BuildConfig

/**
 * Shared bitmap decoding helper for the indexing and embedding pipelines.
 *
 * Decodes to at most [maxDimension] px on the longest side (two-pass with inSampleSize) and
 * applies EXIF rotation, so callers always receive an ARGB_8888 bitmap in the correct
 * orientation without ever loading a full-resolution image into memory.
 *
 * @return Pair of (rotated ARGB_8888 bitmap, inSampleSize that was applied).
 *         The caller is responsible for recycling the bitmap when done.
 */
internal object BitmapDecodeUtils {

    private const val TAG = "BitmapDecodeUtils"

    fun decodeSampledAndRotatedBitmap(
        context: Context,
        uri: Uri,
        maxDimension: Int,
    ): Pair<Bitmap, Int> {
        val t0 = if (BuildConfig.DEBUG) SystemClock.elapsedRealtime() else 0L

        // Pass 1 — read image dimensions only (no pixel data).
        val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, boundsOptions)
        }

        val sampleSize = calculateInSampleSize(
            boundsOptions.outWidth,
            boundsOptions.outHeight,
            maxDimension,
        )

        // Pass 2 — decode pixels at the computed sample size.
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val decoded = context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeOptions)
        } ?: throw IllegalStateException("Cannot open bitmap stream for $uri")

        // Pass 3 — read EXIF orientation from a separate stream (ExifInterface requires seekable).
        val rotationDegrees = context.contentResolver.openInputStream(uri)?.use { stream ->
            val exif = ExifInterface(stream)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        } ?: 0f

        // Apply rotation after downsampling (cheap on the small bitmap).
        val rotated = if (rotationDegrees != 0f) {
            val matrix = Matrix().apply { postRotate(rotationDegrees) }
            Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
                .also { if (it !== decoded) decoded.recycle() }
        } else {
            decoded
        }

        // Guarantee ARGB_8888 — rotation and some decoders may produce a different config.
        // HARDWARE bitmaps (API 26+) also fail config == ARGB_8888, so they are caught here too.
        val argb = if (rotated.config != Bitmap.Config.ARGB_8888) {
            rotated.copy(Bitmap.Config.ARGB_8888, false).also { rotated.recycle() }
        } else {
            rotated
        }

        if (BuildConfig.DEBUG) {
            val elapsed = SystemClock.elapsedRealtime() - t0
            Log.d(TAG, "decode+rotate ${argb.width}x${argb.height} sampleSize=$sampleSize in ${elapsed}ms [$uri]")
        }

        return argb to sampleSize
    }

    /**
     * Returns the smallest power-of-two inSampleSize such that
     * max(width, height) / sampleSize <= maxDimension.
     */
    fun calculateInSampleSize(width: Int, height: Int, maxDimension: Int): Int {
        var sampleSize = 1
        while (maxOf(width / sampleSize, height / sampleSize) > maxDimension) {
            sampleSize *= 2
        }
        return sampleSize
    }
}
