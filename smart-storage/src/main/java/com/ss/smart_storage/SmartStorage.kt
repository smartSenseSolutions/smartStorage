package com.ss.smart_storage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import com.ss.smart_storage.util.PermissionStatus
import com.ss.smart_storage.util.SmartDirectory
import com.ss.smart_storage.util.SmartFileType
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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

class SmartStorage(private val activity: ComponentActivity) {


    private var baseDocumentTreeUri: Uri? = null
    private var fileDetails: FileDetails? = null


   private val  permissionManager = PermissionManager(
       activity = activity,
    onPermissionGranted = { status ->
        when (status) {
            PermissionStatus.NOT_NEEDED -> {
                callFileDetails()
            }

            PermissionStatus.ACCEPTED -> {
                callFileDetails()
            }

            PermissionStatus.DENIED -> {
                Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }

            PermissionStatus.NOT_APPLICABLE -> {
                launchBaseDirectoryPicker()
            }

            PermissionStatus.NOT_AVAILABLE -> {
                Toast.makeText(activity, "Feature not Available", Toast.LENGTH_SHORT).show()
            }

            PermissionStatus.REDIRECT_TO_SETTINGS -> {
                //Redirection
            }
        }
    })


    private val safLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                if (uri != null) {
                    baseDocumentTreeUri = uri
                    if (fileDetails != null) {
                        writeFileToDocumentTree(
                            baseDocumentTreeUri,
                            fileDetails!!.name,
                            fileDetails!!.fileData,
                        )
                    }
                }
            }
        }

    private fun launchBaseDirectoryPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        safLauncher.launch(intent)
    }


    fun store(
        location: String, fileName: String? = null, fileType: SmartFileType, fileData: ByteArray
    ) {
        val name =
            if (!fileName.isNullOrEmpty()) "$fileName.$fileType" else "smartStorage.${fileType.name}"

        fileDetails = FileDetails(
            name = name, location = location, fileType = fileType, fileData = fileData
        )

        permissionManager.checkLocation(location)
    }


    private fun callFileDetails() {
        if (fileDetails != null) {
            storeToDirectory(
                fileDetails = fileDetails!!
            )
        }
    }


    private fun storeToDirectory(fileDetails: FileDetails) {
        val directory = handleFileCreation(fileDetails.location, activity.applicationContext)

        if (directory != null && !directory.exists()) {
            directory.mkdirs()
        }

        val file = File(
            directory, fileDetails.name
        )

        try {
            FileOutputStream(file).use { stream ->
                stream.write(fileDetails.fileData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun writeFileToDocumentTree(
        baseDocumentTreeUri: Uri?, fileName: String, content: ByteArray
    ) {
        baseDocumentTreeUri?.let { treeUri ->
            try {
                val directory = DocumentFile.fromTreeUri(activity.applicationContext, treeUri)
                val file = directory?.createFile("text/*", fileName)
                val pfd = file?.let {
                    activity.applicationContext.contentResolver.openFileDescriptor(
                        it.uri, "w"
                    )
                }
                pfd?.use { descriptor ->
                    val fos = FileOutputStream(descriptor.fileDescriptor)
                    fos.write(content)
                    fos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun handleFileCreation(location: String, context: Context): File? {
        return when (location) {
            SmartDirectory.INTERNAL -> context.filesDir
            SmartDirectory.EXTERNAL_PUBLIC -> Environment.getExternalStoragePublicDirectory("SmartStorage")
            SmartDirectory.SCOPED_STORAGE -> context.getExternalFilesDir(null)
            else -> Environment.getExternalStoragePublicDirectory(location)
        }
    }

}


