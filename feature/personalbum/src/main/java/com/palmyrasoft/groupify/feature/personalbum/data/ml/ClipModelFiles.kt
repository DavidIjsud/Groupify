// feature/personalbum/src/main/.../data/ml/ClipModelFiles.kt
package com.palmyrasoft.groupify.feature.personalbum.data.ml

import android.content.Context
import java.io.File
import java.io.FileOutputStream

/**
 * Materializes a bundled ONNX model asset onto disk so ONNX Runtime can open it by path.
 *
 * The CLIP models live under `assets/clip/`, which aapt may compress; ONNX Runtime's path-based
 * `createSession` needs a real file. We stream-copy (low memory — the text encoder is ~254 MB) into
 * `filesDir` once and reuse it. Copies only when the destination is missing/empty, so subsequent
 * launches are instant.
 */
internal object ClipModelFiles {
    fun ensure(context: Context, assetPath: String): File {
        val dest = File(context.filesDir, assetPath)
        if (dest.exists() && dest.length() > 0L) return dest

        dest.parentFile?.mkdirs()
        context.assets.open(assetPath).use { input ->
            FileOutputStream(dest).use { output -> input.copyTo(output, DEFAULT_BUFFER) }
        }
        return dest
    }

    private const val DEFAULT_BUFFER = 64 * 1024
}
