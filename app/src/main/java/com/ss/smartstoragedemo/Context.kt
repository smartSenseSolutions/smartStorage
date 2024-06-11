package com.ss.smartstoragedemo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast


fun Context.toast(id: Int, length: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), length)
}

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    try {
        if (isOnMainThread()) {
            if (!TextUtils.isEmpty(msg)) {
                Toast.makeText(applicationContext, msg, length).show()
            }
        } else {
            if (!TextUtils.isEmpty(msg)) {
                Handler(Looper.getMainLooper()).post {
                    if (!TextUtils.isEmpty(msg)) {
                        Toast.makeText(applicationContext, msg, length).show()
                    }
                }
            }
        }
    } catch (_: Exception) {}
}

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()


