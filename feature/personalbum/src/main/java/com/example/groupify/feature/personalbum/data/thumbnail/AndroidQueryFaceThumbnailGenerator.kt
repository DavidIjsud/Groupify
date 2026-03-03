package com.example.groupify.feature.personalbum.data.thumbnail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.example.groupify.feature.personalbum.domain.model.QueryFace
import com.example.groupify.feature.personalbum.domain.thumbnail.QueryFaceThumbnailGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.math.max

class AndroidQueryFaceThumbnailGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
) : QueryFaceThumbnailGenerator {

    override suspend fun generate(
        queryPhotoUri: String,
        faces: List<QueryFace>,
    ): Map<Int, String> = withContext(Dispatchers.IO) {
        if (faces.isEmpty()) return@withContext emptyMap()

        val uri = Uri.parse(queryPhotoUri)

        // --- 1. Two-pass decode: measure then decode at a bounded sample size ---
        val boundsOpts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { s ->
            BitmapFactory.decodeStream(s, null, boundsOpts)
        }
        val sampleSize = computeSampleSize(boundsOpts.outWidth, boundsOpts.outHeight, MAX_DIM)

        val decodeOpts = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val rawBitmap = context.contentResolver.openInputStream(uri)?.use { s ->
            BitmapFactory.decodeStream(s, null, decodeOpts)
        } ?: return@withContext emptyMap()

        // --- 2. Apply EXIF rotation (same logic as MlKitFaceDetector) ---
        val rotation = context.contentResolver.openInputStream(uri)?.use { s ->
            val exif = ExifInterface(s)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        } ?: 0f

        val bitmap = if (rotation != 0f) {
            val matrix = Matrix().apply { postRotate(rotation) }
            Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
                .also { if (it !== rawBitmap) rawBitmap.recycle() }
        } else {
            rawBitmap
        }

        // --- 3. Crop a padded square around each face and save as JPEG ---
        val result = mutableMapOf<Int, String>()
        try {
            for (face in faces) {
                val thumbUri = cropAndSave(bitmap, face, sampleSize) ?: continue
                result[face.id] = thumbUri
            }
        } finally {
            bitmap.recycle()
        }
        result
    }

    private fun cropAndSave(bitmap: Bitmap, face: QueryFace, sampleSize: Int): String? {
        val bb = face.boundingBox

        // Scale bounding box to match the downsampled bitmap resolution
        val sLeft = bb.left / sampleSize
        val sTop = bb.top / sampleSize
        val sRight = bb.right / sampleSize
        val sBottom = bb.bottom / sampleSize

        val bbW = sRight - sLeft
        val bbH = sBottom - sTop
        // Expand to a padded square centered on the face
        val side = max(bbW, bbH) * PADDING_FACTOR
        val cx = (sLeft + sRight) / 2f
        val cy = (sTop + sBottom) / 2f

        val cropLeft = (cx - side / 2f).toInt().coerceAtLeast(0)
        val cropTop = (cy - side / 2f).toInt().coerceAtLeast(0)
        val cropRight = (cx + side / 2f).toInt().coerceAtMost(bitmap.width)
        val cropBottom = (cy + side / 2f).toInt().coerceAtMost(bitmap.height)

        val cropW = cropRight - cropLeft
        val cropH = cropBottom - cropTop
        if (cropW <= 0 || cropH <= 0) return null

        val cropped = Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropW, cropH)
        val scaled = Bitmap.createScaledBitmap(cropped, THUMB_SIZE, THUMB_SIZE, true)
        if (cropped !== scaled) cropped.recycle()

        val file = File(context.cacheDir, "face_thumb_${face.id}.jpg")
        return try {
            file.outputStream().use { out ->
                scaled.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file,
            ).toString()
        } finally {
            scaled.recycle()
        }
    }

    private fun computeSampleSize(width: Int, height: Int, maxDim: Int): Int {
        var s = 1
        while (max(width / s, height / s) > maxDim) s *= 2
        return s
    }

    private companion object {
        const val MAX_DIM = 1024
        const val THUMB_SIZE = 128
        const val PADDING_FACTOR = 1.4f
    }
}
