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

        if (isVersionInBetween(Build.VERSION_CODES.M, Build.VERSION_CODES.Q)) {

            checkIfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                onPermissionGranted = {
                    onPermissionGranted(PermissionStatus.ACCEPTED)
                })
        } else if (isVersionInBetween(
                Build.VERSION_CODES.R, Build.VERSION_CODES.S
            ) && location == SmartDirectory.EXTERNAL_PUBLIC
        ) {
            checkIfManagePermissionGranted(Manifest.permission.MANAGE_EXTERNAL_STORAGE ,
                onPermissionGranted = {
                    granted ->
                    if(granted)  onPermissionGranted(PermissionStatus.ACCEPTED)
                    else onPermissionGranted(PermissionStatus.REDIRECT_TO_SETTINGS)
                })


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && location == SmartDirectory.EXTERNAL_PUBLIC) {
            onPermissionGranted(PermissionStatus.NOT_AVAILABLE)
        } else {
            onPermissionGranted(PermissionStatus.ACCEPTED)
        }
    }

    private fun checkIfPermissionGranted(permission: String, onPermissionGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                activity, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionGranted()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun checkIfManagePermissionGranted(permission: String, onPermissionGranted: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                activity, permission
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