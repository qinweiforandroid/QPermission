package com.qw.permission

import kotlin.collections.ArrayList

interface OnResultListener {
    fun onGranted(permissions: ArrayList<String>)
    fun onDeniedAndNeverAskAgain(deniedPermissions: ArrayList<String>)
    fun onShowRequestPermissionRationale(deniedPermissions: ArrayList<String>) {}
}