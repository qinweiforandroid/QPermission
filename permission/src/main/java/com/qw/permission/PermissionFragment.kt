package com.qw.permission

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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

        val permission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                val result = PermissionResult()
                it.forEach { item ->
                    if (item.value) {
                        result.addGrantPermission(item.key)
                    } else {
                        result.addDeniedPermission(item.key)
                    }
                }
                //处理Rationale
                for (permission in result.getDeniedPermissions()) {
                    if (Permission.shouldShowRequestPermissionRationale(
                            context as Activity,
                            permission
                        )
                    ) {
                        permissionsResultListener?.onRequestPermissionRationaleShow(permission)
                        closeSelf()
                        return@registerForActivityResult
                    }
                }
                permissionsResultListener?.onRequestPermissionsResult(result)
            }
        permission.launch(permissions)
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

    fun setOnRequestPermissionsResultListener(listener: OnPermissionsResultListener?) {
        this.permissionsResultListener = listener
    }

    override fun onDetach() {
        super.onDetach()
        this.permissionsResultListener = null
    }

    companion object {
        fun newInstance(permissions: Array<String>): PermissionFragment {
            val fragment = PermissionFragment()
            fragment.arguments = Bundle().apply {
                putStringArray("permissions", permissions)
            }
            return fragment
        }
    }
}