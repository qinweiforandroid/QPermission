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
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mPermissions: Array<String>
    private var requestPermissionsResultListener: OnRequestPermissionsResultListener? = null

    private constructor()


    constructor(activity: FragmentActivity) {
        mFragmentManager = activity.supportFragmentManager
    }

    constructor(fragment: Fragment) {
        mFragmentManager = fragment.childFragmentManager
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

        fun permissionSettings(activity: FragmentActivity, requestCode: Int) {
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

    fun setOnRequestPermissionsResultListener(listener: OnRequestPermissionsResultListener?): Permission {
        this.requestPermissionsResultListener = listener
        return this
    }

    fun request() {
        mFragmentManager
            .beginTransaction()
            .add(PermissionFragment.newInstance(mPermissions).apply {
                this.setOnRequestPermissionsResultListener(requestPermissionsResultListener)
            }, "")
            .commitAllowingStateLoss()
    }
}