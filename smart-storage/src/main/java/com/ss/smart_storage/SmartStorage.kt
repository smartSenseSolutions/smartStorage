package com.ss.smart_storage

import android.app.Activity
import android.content.Context
import android.os.Environment
import com.ss.smart_storage.util.SmartDirectory
import com.ss.smart_storage.util.SmartFileType
import java.io.File
import java.io.FileOutputStream

class SmartStorage(private val activity: Activity) {


    //todo :Can be done location enforce use of SmartFileType sealed class
    fun store(
        location: String, fileName: String? = null, fileType: SmartFileType, fileData: ByteArray
    ) {

        //todo: Add Some Randomizer function in Default Naming of File
        val name =
            if (!fileName.isNullOrEmpty()) "$fileName.$fileType" else "smartStorage.${fileType.name}"

        val directory = handleFileCreation(location, activity.applicationContext)

        val file = File(
            directory, name
        )

        try {
            FileOutputStream(file).use { stream ->
                stream.write(fileData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun handleFileCreation(location: String, context: Context): File? {
        return when (location) {
            SmartDirectory.INTERNAL -> context.filesDir
            SmartDirectory.EXTERNAL_APP -> context.getExternalFilesDir(null)
            else -> Environment.getExternalStoragePublicDirectory(location)
        }
    }

    //todo : have to check how to handle on request permissions result

}


