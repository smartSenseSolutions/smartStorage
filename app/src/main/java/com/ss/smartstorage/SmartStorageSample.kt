package com.ss.smartstorage

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ss.smart_storage.SmartStorage
import com.ss.smart_storage.util.SmartDirectory
import com.ss.smart_storage.util.SmartFileType

@Composable
fun SmartStorageSample(
    context : Context ,
    activity: MainActivity,
    viewModel: SmartStorageViewModel = viewModel()
){

    val smartStorage = SmartStorage(activity)

    fun dummyStore(){
        smartStorage.store(
            location = SmartDirectory.EXTERNAL_APP,
            fileName = "figh",
            fileType = SmartFileType.png,
            fileData = "Fighters Live Forever".toByteArray()
        )
    }

    fun askPermission(){
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101
            )
        }
    }
    
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedButton(onClick = {dummyStore() }) {
        Text(text = "SAVE DUMMY")
        }

        ElevatedButton(onClick = {askPermission()}) {
            Text(text = "GET PERMSSION")
        }
    }
}

