package com.qw.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * Created by qinwei on 2023/7/31 21:21
 * email: qinwei_it@163.com
 */
class PermissionHandler {
    private var context: Context
    private var mFragmentManager: FragmentManager
    private var mPermissions: Array<String> = emptyArray()
    private lateinit var result: PermissionResult
    private var mResultListener: OnResultListener? = null
    private var ing = false

    constructor(activity: FragmentActivity) {
        mFragmentManager = activity.supportFragmentManager
        this.context = activity
    }

    constructor(fragment: Fragment) {
        mFragmentManager = fragment.childFragmentManager
        this.context = fragment.requireContext()
    }

    fun permissions(permissions: Array<String>): PermissionHandler {
        permissions.forEach {
            permission(it)
        }
        return this
    }

    fun permission(permission: String): PermissionHandler {
        if (!mPermissions.contains(permission)) {
            mPermissions = mPermissions.plus(permission)
        }
        return this
    }

    fun setOnResultListener(listener: OnResultListener): PermissionHandler {
        this.mResultListener = listener
        return this
    }

    private fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            (context as Activity).shouldShowRequestPermissionRationale(permission)
        } else false
    }

    private fun handlerPermissionResult() {
        val grantPermissions = ArrayList<String>()
        val deniedPermissions = ArrayList<String>()
        mPermissions.forEach {
            if (ContextCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                grantPermissions.add(it)
            } else {
                deniedPermissions.add(it)
            }
        }
        result = PermissionResult(grantPermissions, deniedPermissions)
    }

    fun request(isReasonBeforeRequest: Boolean = false) {
        if (ing) {
            return
        }
        ing = true
        handlerPermissionResult()
        if (result.isGrant()) {
            //处理已授权场景
            mResultListener?.onGranted(result.grantPermissions)
            return
        }
        if (isReasonBeforeRequest) {
            mResultListener?.onShowRequestPermissionRationale(result.deniedPermissions)
            return
        }
        mFragmentManager.beginTransaction().add(PermissionFragment.newInstance(result).apply {
            this.setOnResultListener(mResultListener)
        }, "PermissionFragment").commitAllowingStateLoss()
    }
}