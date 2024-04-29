package com.ss.smartstorage

import android.annotation.SuppressLint
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

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val smartStorage = SmartStorage(this)


        setContent {
            SmartStorageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SmartStorageSample(
                        onStoreTap = {
                            smartStorage.store(
                                location = SmartDirectory.DOWNLOADS,
                                fileType = SmartFileType.TXT,
                                fileName = "SampleFile.txt",
                                fileData = "This is a sample txt file.".toByteArray()
                            )
                        },
                   )
                }
            }
        }
    }




}

