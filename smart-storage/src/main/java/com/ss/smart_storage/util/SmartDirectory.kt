package com.ss.smart_storage.util

import android.os.Environment


 class SmartDirectory{
    companion object {
        const val INTERNAL: String = "Internal"
        const val EXTERNAL_APP : String = "External_App"
    }

    data object External {
        val music: String = Environment.DIRECTORY_MUSIC
        val podcasts: String = Environment.DIRECTORY_PODCASTS
        val ringtones: String = Environment.DIRECTORY_RINGTONES
        val alarms: String = Environment.DIRECTORY_ALARMS
        val notifications: String = Environment.DIRECTORY_NOTIFICATIONS
        val pictures: String = Environment.DIRECTORY_PICTURES
        val movies: String = Environment.DIRECTORY_MOVIES
        val downloads: String = Environment.DIRECTORY_DOWNLOADS
        val dcim: String = Environment.DIRECTORY_DCIM
        val documents: String = Environment.DIRECTORY_DOCUMENTS
    }
}


