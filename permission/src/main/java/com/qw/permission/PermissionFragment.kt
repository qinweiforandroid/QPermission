package com.qw.permission

import android.os.Bundle
import androidx.fragment.app.Fragment

class PermissionFragment : Fragment() {
    private lateinit var permissions: Array<String>
    private var requestPermissionsResultListener: OnRequestPermissionsResultListener? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            //处理进程在后台被清理场景
            closeSelf()
            return
        }
        permissions = arguments!!.getStringArray("permissions")!!
        requestPermissions(
            permissions,
            REQUEST_PERMISSION_CODE
        )
    }

    private fun closeSelf() {
        if (parentFragment == null) {
            activity!!.supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commitAllowingStateLoss()
        } else {
            parentFragment!!.childFragmentManager.beginTransaction()
                .remove(this)
                .commitAllowingStateLoss()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            requestPermissionsResultListener?.onRequestPermissionsResult(
                PermissionResult(
                    permissions,
                    grantResults
                )
            )
            closeSelf()
        }
    }

    fun setOnRequestPermissionsResultListener(listener: OnRequestPermissionsResultListener?) {
        this.requestPermissionsResultListener = listener
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 200
        fun newInstance(permissions: Array<String>): PermissionFragment {
            val fragment = PermissionFragment()
            fragment.arguments = Bundle().apply {
                putStringArray("permissions", permissions)
            }
            return fragment
        }
    }
}