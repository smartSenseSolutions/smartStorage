package com.ss.smart_storage.util


enum class SmartFileType(val extension: String, val mimeType: String) {
    TXT(extension = ".txt", mimeType = "text/plain"), JPEG(
        extension = ".jpeg", mimeType = "image/jpeg"
    ),
    PNG(extension = ".png", mimeType = "image/png"), PDF(
        extension = ".pdf", mimeType = "application/pdf"
    ),
    WEBP(extension = ".webp", mimeType = "image/webp"),
}
