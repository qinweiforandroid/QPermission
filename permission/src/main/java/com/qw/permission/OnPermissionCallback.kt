package com.qw.permission

interface OnPermissionCallback {

    fun onPermissionGranted(permission: String)

    fun onRequestPermissionRationale(permission: String, scope: PermissionScope)

    fun onPermissionDenied(permission: String, scope: PermissionScope)
}