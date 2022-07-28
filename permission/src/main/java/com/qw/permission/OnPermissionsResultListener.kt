package com.qw.permission

interface OnPermissionsResultListener {
    fun onRequestPermissionsResult(result: PermissionResult)

    fun onShowRequestPermissionRationale(permission: String) {}
}