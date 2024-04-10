package com.ss.smart_storage.util

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi

sealed class SmartDirectory{
    companion object{
        val INTERNAL : String = "Internal"
    }

    data object EXTERNAL : SmartDirectory() {
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

        @RequiresApi(Build.VERSION_CODES.Q)
        val audiobooks: String = Environment.DIRECTORY_AUDIOBOOKS

        @RequiresApi(Build.VERSION_CODES.S)
        val recordings: String = Environment.DIRECTORY_RECORDINGS
    }
}

