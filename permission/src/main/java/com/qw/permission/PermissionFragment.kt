package com.qw.permission

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.qw.utils.Trace

class PermissionFragment : Fragment() {
    private var onPermissionGrantedListener: OnPermissionGrantedListener? = null
    private var onPermissionRationaleListener: OnPermissionRationaleListener? = null
    private var onPermissionDeniedListener: OnPermissionDeniedListener? = null

    private lateinit var permissions: Array<String>
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            //处理进程在后台被清理场景
            closeSelf()
            return
        }
        permissions = arguments!!.getStringArray("permissions")!!
        Trace.d("permission", "onActivityCreated requestPermission $permissions")
        requestPermission(permissions)
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

    private fun requestPermission(permissions: Array<String>) {
        val willRequestPermissions = ArrayList<String>().apply {
            for (permission in permissions) {
                val result = ContextCompat.checkSelfPermission(requireContext(), permission)
                if (result != PermissionChecker.PERMISSION_GRANTED) {
                    add(permission)
                    return
                }
            }
        }
        if (willRequestPermissions.size > 0) {
            requestPermissions(
                willRequestPermissions.toArray() as Array<out String>,
                REQUEST_PERMISSION_CODE
            )
        } else {
            permissionGranted()
        }
    }

    private fun permissionGranted() {
        Trace.d("permission", "granted:$permissions")
        onPermissionGrantedListener?.onPermissionGranted(permissions)
        closeSelf()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            val deniedPermissions = ArrayList<String>()
            for (i in permissions.indices) {
                if (grantResults[i] != PermissionChecker.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i])
                }
            }
            if (deniedPermissions.size == 0) {
                permissionGranted()
                return
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    deniedPermissions[0]
                )
            ) {
                showRequestPermissionRationale(arrayOf(deniedPermissions[0]))
            } else {
                permissionDenied()
            }
        }
    }

    private fun permissionDenied() {
        Trace.d("permission", "onPermissionDenied:${this.permissions}")
        val scope = PermissionScope().apply {
            showTask = Runnable {
                AlertDialog.Builder(requireContext())
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setNegativeButton(leftText) { dialog, which ->
                        leftListener?.onClick(dialog, which)
                        //2、关闭当前权限申请的fragment
                        closeSelf()
                    }
                    .setPositiveButton(rightText) { dialog, which ->
                        rightListener?.run {
                            //1、开发者自行处理申请权限
                            onClick(dialog, which)
                            //2、关闭当前权限申请的fragment
                            closeSelf()
                        } ?: kotlin.run {
                            //跳转到应用权限设置页面
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", requireContext().packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, 1)
                        }
                    }.show()
            }
        }
        onPermissionDeniedListener?.onPermissionDenied(permissions, scope)
    }

    private fun showRequestPermissionRationale(permissions: Array<String>) {
        Trace.d("permission", "onShowRequestPermissionRationale:${this.permissions}")
        val scope = PermissionScope().apply {
            this.showTask = Runnable {
                AlertDialog.Builder(requireContext())
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setNegativeButton(leftText) { dialog, which ->
                        leftListener?.onClick(dialog, which)
                        //2、关闭当前权限申请的fragment
                        closeSelf()
                    }
                    .setPositiveButton(rightText) { dialog, which ->
                        rightListener?.run {
                            //1、开发者自行处理申请权限
                            onClick(dialog, which)
                            //2、关闭当前权限申请的fragment
                            closeSelf()
                        } ?: kotlin.run {
                            //触发框架申请权限
                            requestPermission(permissions)
                        }
                    }.show()
            }
        }
        onPermissionRationaleListener?.onPermissionRationale(permissions, scope)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            requestPermission(permissions)
        }
    }


    fun setOnPermissionDenied(onPermissionDeniedListener: OnPermissionDeniedListener) {
        this.onPermissionDeniedListener = onPermissionDeniedListener
    }

    fun setOnPermissionRationale(onPermissionRationaleListener: OnPermissionRationaleListener) {
        this.onPermissionRationaleListener = onPermissionRationaleListener
    }

    fun setOnPermissionGranted(onPermissionGrantedListener: OnPermissionGrantedListener) {
        this.onPermissionGrantedListener = onPermissionGrantedListener
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 20
        fun newInstance(permissions: Array<String>): PermissionFragment {
            val fragment = PermissionFragment()
            val args = Bundle()
            args.putStringArray("permissions", permissions)
            fragment.arguments = args
            return fragment
        }
    }
}