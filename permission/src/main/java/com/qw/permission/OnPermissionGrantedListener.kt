package com.qw.permission

/**
 * Created by qinwei on 2021/7/4 10:15 下午
 * email: qinwei_it@163.com
 */
interface OnPermissionGrantedListener {
    fun onPermissionGranted(permissions: Array<String>)
}