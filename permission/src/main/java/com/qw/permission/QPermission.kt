package com.qw.permission

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.qw.utils.Trace

public class QPermission {
    private var onPermissionGrantedListener: OnPermissionGrantedListener? = null
    private var onPermissionRationaleListener: OnPermissionRationaleListener? = null
    private var onPermissionDeniedListener: OnPermissionDeniedListener? = null
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mPermissions: Array<String>

    private constructor()
    constructor(activity: FragmentActivity) {
        mFragmentManager = activity.supportFragmentManager
    }

    constructor(fragment: Fragment) {
        mFragmentManager = fragment.childFragmentManager
    }

    companion object {
        fun init(activity: FragmentActivity): QPermission {
            return QPermission(activity)
        }

        fun init(fragment: Fragment): QPermission {
            return QPermission(fragment)
        }
    }

    fun permissions(permissions: Array<String>): QPermission {
        this.mPermissions = permissions
        return this
    }

    fun checkSelfPermission(context: Context, permission: String): Int {
        return ContextCompat.checkSelfPermission(context, permission)
    }


    fun setOnPermissionDenied(onPermissionDeniedListener: OnPermissionDeniedListener): QPermission {
        this.onPermissionDeniedListener = onPermissionDeniedListener
        return this
    }

    fun setOnPermissionRationale(onPermissionRationaleListener: OnPermissionRationaleListener): QPermission {
        this.onPermissionRationaleListener = onPermissionRationaleListener
        return this
    }

    fun setOnPermissionGranted(onPermissionGrantedListener: OnPermissionGrantedListener): QPermission {
        this.onPermissionGrantedListener = onPermissionGrantedListener
        return this
    }

    fun request() {
        Trace.d("permission", "request:${mPermissions}")
        mFragmentManager
            .beginTransaction()
            .add(PermissionFragment.newInstance(mPermissions).apply {
                onPermissionDeniedListener?.let {
                    this.setOnPermissionDenied(it)
                }
                onPermissionRationaleListener?.let {
                    this.setOnPermissionRationale(it)
                }
                onPermissionGrantedListener?.let {
                    this.setOnPermissionGranted(it)
                }
            }, "")
            .commitAllowingStateLoss()
    }
}