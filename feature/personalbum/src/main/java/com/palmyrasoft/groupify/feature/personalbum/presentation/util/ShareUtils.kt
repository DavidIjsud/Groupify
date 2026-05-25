package com.palmyrasoft.groupify.feature.personalbum.presentation.util

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.palmyrasoft.groupify.feature.personalbum.R

/**
 * Builds and launches the system multi-image share chooser for [uris].
 * Shared by the results screen and the group-detail screen so both share identically.
 */
fun shareImageUris(context: Context, uris: List<String>) {
    if (uris.isEmpty()) return
    val parsed = uris.map { Uri.parse(it) }
    val clip = ClipData.newUri(context.contentResolver, "Image", parsed.first())
        .also { c -> parsed.drop(1).forEach { uri -> c.addItem(ClipData.Item(uri)) } }
    val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
        type = "image/*"
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(parsed))
        clipData = clip
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(
        Intent.createChooser(intent, context.getString(R.string.photomatch_share_via)),
    )
}
