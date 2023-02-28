package io.ramani.ramaniStationary.app.common.download

import android.net.Uri

interface IFilesDownloadManager {
    fun enqueue(url: String, fileName: String, mimeType: String, onComplete: (Uri) -> Unit)
}