package com.ss.smartstorage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment


@Composable
fun SmartStorageSample(
    onStoreTap : () -> Unit,
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedButton(onClick = {onStoreTap()}) {
        Text(text = "SAVE DUMMY") }

    }
}

