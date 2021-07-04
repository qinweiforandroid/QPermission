package com.qw.permission

/**
 * Created by qinwei on 2021/7/4 10:28 下午
 * email: qinwei_it@163.com
 */
interface OnPermissionDeniedListener {
    fun onPermissionDenied(grantedPermissions: Array<String>, scope: PermissionScope)
}