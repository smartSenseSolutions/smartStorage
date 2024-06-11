package com.ss.smartstorage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.ss.smartstorage.model.FileDetails
import com.ss.smartstorage.util.PermissionStatus
import com.ss.smartstorage.util.SmartDirectory
import com.ss.smartstorage.util.SmartFileType
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class SmartStorage(private val activity: ComponentActivity) {

    private var baseDocumentTreeUri: Uri? = null
    private lateinit var fileDetails: FileDetails
    private var listener: OutputListener? = null

    fun registerListener(listener: OutputListener) {
        this.listener = listener
    }

    @SuppressLint("NewApi")
    private val permissionManager =
        PermissionManager(activity = activity, onPermissionGranted = { status ->

            when (status) {
                PermissionStatus.NOT_NEEDED -> {
                    callFileDetails()
                }

                PermissionStatus.ACCEPTED -> {
                    callFileDetails()
                }

                PermissionStatus.DENIED -> {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.perm_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                PermissionStatus.NOT_APPLICABLE -> {
                    launchBaseDirectoryPicker()
                }

                PermissionStatus.REDIRECT_TO_SETTINGS -> {
                    requestFullStorageAccess()
                }
            }
        })


    private val safLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                if (uri != null) {
                    baseDocumentTreeUri = uri
                    writeFileToDocumentTree(
                        baseDocumentTreeUri,
                    )
                }
            }
        }

    private fun launchBaseDirectoryPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        safLauncher.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestFullStorageAccess(){
        activity.startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
    }


    fun store(
        location: String, fileName: String? = null, fileType: SmartFileType, fileData: ByteArray
    ) {

        val name =
            if(fileName.isNullOrEmpty()) randomFileName(fileType.extension) else fileName.plus(fileType.extension)

        fileDetails = FileDetails(
            name = name, location = location, fileType = fileType, fileData = fileData
        )

        permissionManager.checkLocation(location)
    }


    private fun callFileDetails() {
        storeToDirectory(
            fileDetails = fileDetails
        )
    }

    private fun randomFileName(extension : String): String {
        val alphanumericChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..8)
            .map { alphanumericChars.random() }
            .joinToString("")
        return randomString.plus(extension)
    }


    private fun storeToDirectory(fileDetails: FileDetails) {
        val directory = handleFileCreation(fileDetails.location, activity.applicationContext)

        if (directory != null && !directory.exists()) {
            directory.mkdirs()
        }

        val file = File(
            directory, fileDetails.name
        )
        Log.d("Path to directory:", directory!!.path)
        try {
            FileOutputStream(file).use { stream ->
                stream.write(fileDetails.fileData)
            }
            listener?.onSuccess(file.path)
        } catch (e: Exception) {
            e.printStackTrace()
            listener?.onFail(e.message)
        }
    }

    private fun writeFileToDocumentTree(
        baseDocumentTreeUri: Uri?
    ) {
        baseDocumentTreeUri?.let { treeUri ->
            try {
                val directory = DocumentFile.fromTreeUri(activity.applicationContext, treeUri)
                val file =
                    directory?.createFile(fileDetails.fileType.mimeType, fileDetails.name)
                val pfd = file?.let {
                    activity.applicationContext.contentResolver.openFileDescriptor(
                        it.uri, "w"
                    )
                }
                pfd?.use { descriptor ->
                    val fos = FileOutputStream(descriptor.fileDescriptor)
                    fos.write(fileDetails.fileData)
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
            SmartDirectory.EXTERNAL_PUBLIC -> Environment.getExternalStoragePublicDirectory(
                getPackage()
            )

            SmartDirectory.SCOPED_STORAGE -> context.getExternalFilesDir(null)
            else -> Environment.getExternalStoragePublicDirectory(location)
        }
    }

    private fun getPackage(): String = activity.packageName.substringAfterLast('.')


}


