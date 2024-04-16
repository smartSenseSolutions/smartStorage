package com.ss.smartstorage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment


@Composable
fun SmartStorageSample(
    onDownloadTap : () -> Unit,
    onDocumentTap : () -> Unit,
    onExternalAppTap : () -> Unit,
    onSAFTap : () -> Unit,
    onPermissionGrant : () -> Unit,
    onExternalStorageFolder : () -> Unit,
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedButton(onClick = {onDownloadTap()}) {
        Text(text = "Downloads") }

        ElevatedButton(onClick = {onDocumentTap()}) {
            Text(text = "Documents") }

        ElevatedButton(onClick = {onExternalAppTap()}) {
            Text(text = "External App Tap") }

        ElevatedButton(onClick = {onSAFTap()}) {
            Text(text = "SAF TAP") }

        ElevatedButton(onClick = {onPermissionGrant()}) {
            Text(text = "Grant WRITE Permission") }

        ElevatedButton(onClick = {onExternalStorageFolder()}) {
            Text(text = "External Storage Custom Foler") }

    }
}

