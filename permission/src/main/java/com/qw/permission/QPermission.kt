package com.qw.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class QPermission {

    companion object {
        fun init(activity: FragmentActivity): PermissionHandler {
            return PermissionHandler(activity)
        }

        fun init(fragment: Fragment): PermissionHandler {
            return PermissionHandler(fragment)
        }

        fun checkSelfPermission(context: Context, permission: String): Int {
            return ContextCompat.checkSelfPermission(context, permission)
        }

        fun isGrant(context: Context, permission: String): Boolean {
            return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        fun shouldShowRequestPermissionRationale(
            activity: Activity, permission: String
        ): Boolean {
            return if (Build.VERSION.SDK_INT >= 23) {
                activity.shouldShowRequestPermissionRationale(permission)
            } else false
        }

        fun areNotificationsEnabled(context: Context): Boolean {
            return NotificationManagerCompat.from(context).areNotificationsEnabled()
        }

        fun settings(activity: FragmentActivity, requestCode: Int) {
            //跳转到应用权限设置页面
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivityForResult(intent, requestCode)
        }
    }
}