package com.ss.smartstorage

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ss.smart_storage.OutputListener
import com.ss.smart_storage.SmartStorage
import com.ss.smartstorage.ui.theme.SmartStorageTheme

class MainActivity : ComponentActivity(), OutputListener {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val smartStorage = SmartStorage(this)
        smartStorage.registerListener(this)
        setContent {
            SmartStorageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SmartStorageSample(
                        onStoreTap = { fileName, fileType, location ->
                                smartStorage.store(
                                    location = location,
                                    fileType = fileType,
                                    fileName = fileName,
                                    fileData = "This is a sample txt file.".toByteArray()
                                )
                        }
                   )
                }
            }
        }
    }

    override fun onSuccess(result: String?) {
        result?.let { toast(it) }
        Log.e("MainAct", "onSuccess result: $result")
    }

    override fun onFail(error: String?) {
        error?.let { toast(it) }
        Log.e("MainAct", "onFail error: $error")
    }


}

