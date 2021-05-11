package com.qw.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.qw.utils.Trace

public object PermissionHelper {
    fun requestPermission(activity: FragmentActivity, permission: String) {
        requestPermission(activity.supportFragmentManager, permission)
    }

    fun requestPermission(fragment: Fragment, permission: String) {
        requestPermission(fragment.childFragmentManager, permission)
    }

    fun checkSelfPermission(context: Context,permission: String): Int {
       return  ContextCompat.checkSelfPermission(context,permission)
    }

    private fun requestPermission(supportFragmentManager: FragmentManager, permission: String) {
        Trace.d("permission","requestPermission:${permission}")
        supportFragmentManager
                .beginTransaction()
                .add(PermissionFragment.newInstance(permission), "")
                .commitAllowingStateLoss()
    }
}