package com.ss.smartstoragedemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ss.smartstorage.util.SmartDirectory
import com.ss.smartstorage.util.SmartFileType
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartStorageToolBar() {
    TopAppBar(
        title = {
            Text(
                text = "Smart Storage Demo",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        },

        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartStorageSample(
    onStoreTap: (String, SmartFileType, String) -> Unit
) {
    var showBottomSheetD by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var selectedDestination by remember {
        mutableStateOf("")
    }

    var isSaveButtonVisible by remember {
        mutableStateOf(false)
    }

    var showBottomSheetF by remember {
        mutableStateOf(false)
    }
    var fileName by remember {
        mutableStateOf("")
    }

    var selectedFiletype by remember {
        mutableStateOf(SmartFileType.TXT)
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = { SmartStorageToolBar() }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, content = { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 30.dp, top = 10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "File Name : ")
                    Spacer(modifier = Modifier.height(16.dp))

                    Row {

                        TextField(
                            value = fileName,
                            onValueChange = {
                                fileName = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth()
                        )


                    }
                }
                Spacer(modifier = Modifier.height(20.dp))


                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "File Storage Type : ")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(onClick = { showBottomSheetF = true }) {
                        Text(text = selectedFiletype.toString().ifEmpty { "Select" })

                    }

                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "File Storage Destination : ")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(onClick = { showBottomSheetD = true }) {
                        Text(text = selectedDestination.ifEmpty { "Select" })

                    }

                }

                Spacer(modifier = Modifier.height(40.dp))



                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        enabled = isSaveButtonVisible, onClick = {
                            onStoreTap(fileName, selectedFiletype, selectedDestination)
                        }, modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Save")
                    }
                }
            }

        }
    })
    if (showBottomSheetF) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheetF = false },
            sheetState = sheetState,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select File Type to store File",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                val fileTypes = listOf(
                    SmartFileType.TXT,
                    SmartFileType.JPEG,
                    SmartFileType.PDF,
                    SmartFileType.PNG,
                    SmartFileType.WEBP
                )

                fileTypes.forEach { fileType ->
                    TextButton(onClick = {
                        selectedFiletype = fileType
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheetF = false
                            }
                        }
                    }) {
                        Text(text = fileType.toString())
                    }
                }

            }


        }
    }

    if (showBottomSheetD) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheetD = false },
            sheetState = sheetState,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Destination to store File",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                val destinations = listOf(
                    SmartDirectory.INTERNAL,
                    SmartDirectory.SCOPED_STORAGE,
                    SmartDirectory.CUSTOM,
                    SmartDirectory.DOWNLOADS,
                    SmartDirectory.DOCUMENTS,
                    SmartDirectory.EXTERNAL_PUBLIC
                )

                destinations.forEach { destination ->
                    TextButton(onClick = {
                        selectedDestination = destination
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                isSaveButtonVisible = true
                                showBottomSheetD = false
                            }
                        }
                    }) {
                        Text(text = destination)
                    }
                }

            }


        }
    }
}





