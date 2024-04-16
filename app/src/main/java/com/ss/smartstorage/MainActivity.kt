package com.ss.smartstorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ss.smart_storage.SmartStorage
import com.ss.smart_storage.util.SmartDirectory
import com.ss.smart_storage.util.SmartFileType
import com.ss.smartstorage.ui.theme.SmartStorageTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val smartStorage = SmartStorage(this)

        val fileName = "testFile2"
        val fileContent = "This is a test file"

        setContent {
            SmartStorageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                        SmartStorageSample(
                            onDownloadTap = {
                                smartStorage.store(
                                    location = SmartDirectory.DOWNLOADS,
                                    fileName = fileName,
                                    fileType = SmartFileType.txt,
                                    fileData = fileContent.toByteArray()
                                )
                            },
                            onDocumentTap = {
                                smartStorage.store(
                                    location = SmartDirectory.DOCUMENTS,
                                    fileName = fileName,
                                    fileType = SmartFileType.txt,
                                    fileData = fileContent.toByteArray()
                                )
                            },
                            onExternalAppTap = {
                                smartStorage.store(
                                    location = SmartDirectory.EXTERNAL_APP,
                                    fileName = fileName,
                                    fileType = SmartFileType.txt,
                                    fileData = fileContent.toByteArray()
                                )
                            },
                            onSAFTap = {
                                smartStorage.store(
                                    location = SmartDirectory.CUSTOM,
                                    fileName = fileName,
                                    fileType = SmartFileType.txt,
                                    fileData = fileContent.toByteArray()
                                )
                            },
                            onExternalStorageFolder = {
                                smartStorage.store(
                                    location = SmartDirectory.EXTERNAL,
                                    fileName = fileName,
                                    fileType = SmartFileType.txt,
                                    fileData = fileContent.toByteArray()
                                )
                            },
                            onPermissionGrant = {
                                smartStorage.grantExternalStoragePermission()
                            },

                        )
                }
                }
            }
        }
    }






