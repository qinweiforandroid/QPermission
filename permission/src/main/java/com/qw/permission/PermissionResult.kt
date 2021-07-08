package com.qw.permission

import androidx.core.content.PermissionChecker

class PermissionResult(permissions: Array<String>, grantResults: IntArray) {
    private val deniedPermissions = ArrayList<String>()
    private val grantPermissions = ArrayList<String>()

    init {
        for (i in permissions.indices) {
            if (grantResults[i] == PermissionChecker.PERMISSION_GRANTED) {
                grantPermissions.add(permissions[i])
            } else {
                deniedPermissions.add(permissions[i])
            }
        }
    }

    fun isGrant(): Boolean {
        return deniedPermissions.size == 0
    }

    fun getDeniedPermissions(): ArrayList<String> {
        return deniedPermissions
    }
}