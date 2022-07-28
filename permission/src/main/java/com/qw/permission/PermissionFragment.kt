package com.qw.permission

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class PermissionFragment : Fragment() {
    private lateinit var permissions: Array<String>
    private var permissionsResultListener: OnPermissionsResultListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            //处理进程在后台被清理场景
            closeSelf()
            return null
        }
        permissions = requireArguments().getStringArray("permissions")!!
        requestPermissions(
            permissions,
            REQUEST_PERMISSION_CODE
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun closeSelf() {
        if (parentFragment == null) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commitAllowingStateLoss()
        } else {
            requireParentFragment().childFragmentManager.beginTransaction()
                .remove(this)
                .commitAllowingStateLoss()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            //处理永久拒绝场景
            for (mPermission in permissions) {
                if (Permission.shouldShowRequestPermissionRationale(
                        context as Activity,
                        mPermission
                    )
                ) {
                    permissionsResultListener?.onShowRequestPermissionRationale(mPermission)
                    closeSelf()
                    return
                }
            }
            permissionsResultListener?.onRequestPermissionsResult(
                PermissionResult(
                    permissions,
                    grantResults
                )
            )
            closeSelf()
        }
    }

    fun setOnRequestPermissionsResultListener(listener: OnPermissionsResultListener?) {
        this.permissionsResultListener = listener
    }

    override fun onDetach() {
        super.onDetach()
        this.permissionsResultListener = null
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