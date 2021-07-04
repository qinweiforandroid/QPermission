package com.qw.permission

/**
 * Created by qinwei on 2021/7/4 10:17 下午
 * email: qinwei_it@163.com
 */
interface OnPermissionRationaleListener {
    fun onPermissionRationale(permissions: Array<String>, scope: PermissionScope)
}