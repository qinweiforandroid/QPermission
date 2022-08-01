package com.qw.permission

interface OnPermissionsResultListener {
    fun onRequestPermissionsResult(result: PermissionResult)

    fun onRequestPermissionRationaleShow(permission: String) {}
}