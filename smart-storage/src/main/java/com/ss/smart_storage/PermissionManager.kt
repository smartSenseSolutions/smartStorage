package com.ss.smart_storage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.Q
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.ss.smart_storage.util.PermissionStatus
import com.ss.smart_storage.util.SmartDirectory


class PermissionManager(
    private val activity: ComponentActivity, val onPermissionGranted: (PermissionStatus) -> Unit
) {
    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted(PermissionStatus.ACCEPTED)
        } else {
            onPermissionGranted(PermissionStatus.DENIED)
        }
    }

    fun checkLocation(location: String) {
        when (location) {

            SmartDirectory.CUSTOM -> {
                onPermissionGranted(PermissionStatus.NOT_APPLICABLE)
            }

            SmartDirectory.INTERNAL -> {
                onPermissionGranted(PermissionStatus.NOT_NEEDED)
            }

            SmartDirectory.DOCUMENTS -> {
                checkOsForPermissions(location)
            }

            SmartDirectory.DOWNLOADS -> {
                checkOsForPermissions(location)
            }

            SmartDirectory.SCOPED_STORAGE -> {
                onPermissionGranted(PermissionStatus.NOT_NEEDED)
            }

            SmartDirectory.EXTERNAL_PUBLIC -> {
                checkOsForPermissions(location)
            }
        }
    }

    private fun checkOsForPermissions(location: String) {
        when {
            Build.VERSION.SDK_INT in M..Q -> {
                checkIfPermissionGranted(onPermissionGranted = {
                    onPermissionGranted(PermissionStatus.ACCEPTED)
                })
            }

            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) && location == SmartDirectory.EXTERNAL_PUBLIC -> {

                checkIfManagePermissionGranted(onPermissionGranted = { granted ->
                    if (granted) onPermissionGranted(PermissionStatus.ACCEPTED)
                    else onPermissionGranted(PermissionStatus.REDIRECT_TO_SETTINGS)
                })


            }

            else -> {
                onPermissionGranted(PermissionStatus.ACCEPTED)
            }
        }
    }


    private fun checkIfPermissionGranted(onPermissionGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionGranted()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkIfManagePermissionGranted(onPermissionGranted: (Boolean) -> Unit) {
        if (Environment.isExternalStorageManager()) {
            onPermissionGranted(true)
        } else {
            onPermissionGranted(false)
        }
    }


}