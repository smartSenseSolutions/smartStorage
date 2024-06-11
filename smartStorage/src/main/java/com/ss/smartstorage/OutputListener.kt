package com.ss.smartstorage

interface OutputListener {
    fun onSuccess(result: String?)
    fun onFail(error: String?)
}