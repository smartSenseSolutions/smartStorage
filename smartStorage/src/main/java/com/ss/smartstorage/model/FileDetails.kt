package com.ss.smartstorage.model

import com.ss.smartstorage.util.SmartFileType

data class FileDetails(
    val name: String,
    val location: String,
    val fileType: SmartFileType,
    val fileData: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as FileDetails
        return fileData.contentEquals(other.fileData)
    }

    override fun hashCode(): Int {
        return fileData.contentHashCode()
    }
}
