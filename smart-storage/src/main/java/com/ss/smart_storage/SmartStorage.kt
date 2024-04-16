package com.ss.smart_storage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
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
    private var fileDetails : FileDetails? = null


    private val safLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            if (uri != null) {
                baseDocumentTreeUri = uri
                if(fileDetails!=null){
                    writeFileToDocumentTree(
                        baseDocumentTreeUri,
                        fileDetails!!.name,
                        fileDetails!!.fileData,
                    )
                }
            }
        }
    }

    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
           // callFileDetails()
            Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun grantExternalStoragePermission(){
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }


    private fun launchBaseDirectoryPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        safLauncher.launch(intent)
    }


//todo : Remove Supress Lint
    @SuppressLint("ObsoleteSdkInt")
    fun isWritePermissionGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                callFileDetails()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        } else {
            callFileDetails()
        }
    }

    private fun callFileDetails(){
        if(fileDetails != null){
            if(fileDetails!!.location == SmartDirectory.CUSTOM){
                launchBaseDirectoryPicker()
            }

            else{
                storeToDirectory(
                    fileDetails = fileDetails!!
                )
            }
        }
    }


    fun store(
        location: String, fileName: String? = null, fileType: SmartFileType, fileData: ByteArray
    ) {
        val name =
            if (!fileName.isNullOrEmpty()) "$fileName.$fileType" else "smartStorage.${fileType.name}"

        fileDetails = FileDetails(
            name = name,
            location = location,
            fileType = fileType,
            fileData = fileData
        )

        callFileDetails()
       // isWritePermissionGranted()
    }

    private fun storeToDirectory(fileDetails: FileDetails){
        val directory = handleFileCreation(fileDetails.location, activity.applicationContext)

        if(directory!=null && !directory.exists()){

             val result =  directory.mkdirs()
            Toast.makeText(activity, "Folder Creation Result $result", Toast.LENGTH_SHORT).show()

        }

        val file = File(
            directory, fileDetails.name
        )

        try {
            FileOutputStream(file).use { stream ->
                stream.write(fileDetails.fileData)

                Toast.makeText(activity, "File made successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(activity, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeFileToDocumentTree(baseDocumentTreeUri: Uri?, fileName: String, content: ByteArray) {
        baseDocumentTreeUri?.let { treeUri ->
            try {
                val directory = DocumentFile.fromTreeUri(activity.applicationContext, treeUri)
                val file = directory?.createFile("text/*", fileName)
                val pfd = file?.let { activity.applicationContext.contentResolver.openFileDescriptor(it.uri, "w") }
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
            SmartDirectory.EXTERNAL_APP -> context.getExternalFilesDir(null)
            SmartDirectory.EXTERNAL -> File(Environment.getExternalStorageDirectory() , "SmartStorage")
            else -> Environment.getExternalStoragePublicDirectory(location)
        }
    }

}


