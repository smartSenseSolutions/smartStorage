package com.ss.smartstoragedemo

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel

class SmartStorageViewModel : ViewModel() {
    private val state = mutableIntStateOf(0)
}