package com.qw.permission

class PermissionResult {
    private val deniedPermissions = ArrayList<String>()
    private val grantPermissions = ArrayList<String>()

    fun addDeniedPermission(permission: String) {
        if (!deniedPermissions.contains(permission)) {
            deniedPermissions.add(permission)
        }
    }

    fun addGrantPermission(permission: String) {
        if (!grantPermissions.contains(permission)) {
            grantPermissions.add(permission)
        }
    }

    fun isGrant(): Boolean {
        return deniedPermissions.size == 0
    }

    fun getDeniedPermissions(): ArrayList<String> {
        return deniedPermissions
    }
}