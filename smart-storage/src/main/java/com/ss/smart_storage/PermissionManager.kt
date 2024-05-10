package com.ss.smart_storage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.ss.smart_storage.util.PermissionStatus
import com.ss.smart_storage.util.SmartDirectory


class PermissionManager(
    private val activity: ComponentActivity,
    val onPermissionGranted: (PermissionStatus) -> Unit
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
            isVersionInBetween(Build.VERSION_CODES.M, Build.VERSION_CODES.Q) -> {
                checkIfPermissionGranted(
                    onPermissionGranted = {
                        onPermissionGranted(PermissionStatus.ACCEPTED)
                    })
            }
            isVersionInBetween(
                Build.VERSION_CODES.R, Build.VERSION_CODES.S
            ) && location == SmartDirectory.EXTERNAL_PUBLIC -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    checkIfManagePermissionGranted(
                        onPermissionGranted = { granted ->
                            if(granted)  onPermissionGranted(PermissionStatus.ACCEPTED)
                            else onPermissionGranted(PermissionStatus.REDIRECT_TO_SETTINGS)
                        })
                }

            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && location == SmartDirectory.EXTERNAL_PUBLIC -> {
                onPermissionGranted(PermissionStatus.NOT_AVAILABLE)
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
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE,)
        }
    }

    private fun checkIfManagePermissionGranted( onPermissionGranted: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.MANAGE_EXTERNAL_STORAGE ,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionGranted(true)
        } else {
            onPermissionGranted(false)
        }
    }




    private fun isVersionInBetween(min: Int, max: Int): Boolean {
        return Build.VERSION.SDK_INT in min..max
    }


}