package com.qw.permission

import java.io.Serializable

data class PermissionResult(
    val grantPermissions: ArrayList<String>,
    val deniedPermissions: ArrayList<String>
) : Serializable {
    fun isGrant(): Boolean {
        return deniedPermissions.size == 0
    }
}