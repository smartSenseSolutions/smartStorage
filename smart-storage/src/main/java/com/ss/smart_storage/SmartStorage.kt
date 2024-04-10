package com.ss.smart_storage

import android.content.Context
import android.util.Log

class SmartStorage(private val  context : Context) {

    private val androidOs = android.os.Build.VERSION.SDK_INT

    fun store(location : String , fileName : String? = null, fileData : ByteArray){
        //todo : Write code to store file
        Log.i("SSLog" , "Work Started")
    }

}
