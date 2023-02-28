package io.ramani.ramaniStationary.app.common.download

import android.net.Uri

interface IMediaDownloadManager {
    fun enqueue(
        downloadUrl: String,
        mediaType: Int,
        fileName: String,
        needsAuth: Boolean,
        onDownloadComplete: (Uri) -> Unit
    )
}