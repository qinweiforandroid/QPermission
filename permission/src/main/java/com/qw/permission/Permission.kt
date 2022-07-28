package com.qw.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class Permission {
    private var mFragmentManager: FragmentManager
    private var mPermissions: Array<String> = emptyArray()
    private var permissionsResultListener: OnPermissionsResultListener? = null

    private var context: Context

    constructor(activity: FragmentActivity) {
        mFragmentManager = activity.supportFragmentManager
        this.context = activity
    }

    constructor(fragment: Fragment) {
        mFragmentManager = fragment.childFragmentManager
        this.context = fragment.requireContext()
    }

    companion object {
        fun init(activity: FragmentActivity): Permission {
            return Permission(activity)
        }

        fun init(fragment: Fragment): Permission {
            return Permission(fragment)
        }

        fun checkSelfPermission(context: Context, permission: String): Int {
            return ContextCompat.checkSelfPermission(context, permission)
        }

        fun isGrant(context: Context, permission: String): Boolean {
            return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        fun shouldShowRequestPermissionRationale(
            activity: Activity,
            permission: String
        ): Boolean {
            return if (Build.VERSION.SDK_INT >= 23) {
                activity.shouldShowRequestPermissionRationale(permission)
            } else false
        }

        fun settings(activity: FragmentActivity, requestCode: Int) {
            //跳转到应用权限设置页面
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivityForResult(intent, requestCode)
        }
    }

    fun permissions(permissions: Array<String>): Permission {
        this.mPermissions = permissions
        return this
    }

    fun permission(permission: String): Permission {
        mPermissions = mPermissions.plus(permission)
        return this
    }

    fun setOnPermissionsResultListener(listener: OnPermissionsResultListener): Permission {
        this.permissionsResultListener = listener
        return this
    }

    fun request() {
        var isGrant = true
        for (mPermission in mPermissions) {
            if (!isGrant(context, mPermission)) {
                isGrant = false
                break
            }
        }
        if (isGrant) {
            //处理已授权场景
            val result = PermissionResult(mPermissions, IntArray(mPermissions.size).apply {
                mPermissions.forEachIndexed { index, _ ->
                    this[index] = PackageManager.PERMISSION_GRANTED
                }
            })
            permissionsResultListener?.onRequestPermissionsResult(result)
            return
        }
        //处理永久拒绝场景
        for (mPermission in mPermissions) {
            if (shouldShowRequestPermissionRationale(context as Activity, mPermission)) {
                permissionsResultListener?.onShowRequestPermissionRationale(mPermission)
                return
            }
        }
        mFragmentManager
            .beginTransaction()
            .add(PermissionFragment.newInstance(mPermissions).apply {
                this.setOnRequestPermissionsResultListener(permissionsResultListener)
            }, "PermissionFragment")
            .commitAllowingStateLoss()
    }
}