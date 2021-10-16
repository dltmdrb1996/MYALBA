package com.bottotop.core.util.test.utils3.utils

import android.webkit.MimeTypeMap
import java.io.File

fun getMimeType(uri: String): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(uri)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

fun getFileType(uri: String): String? {
    return getMimeType(uri)
}

fun getFileSize(uri: String): Long {
    return File(uri).length()
}
