package com.ss.smart_storage

interface OutputListener {
    fun onSuccess(result: String?)
    fun onFail(error: String?)
}