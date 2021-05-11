package com.qw.permission

import android.Manifest
import android.app.AlertDialog
import android.content.Context
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
    private var callback: OnPermissionCallback? = null
    private lateinit var permission: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            //处理进程在后台被清理场景
            closeSelf()
            return
        }
        permission = arguments!!.getString("permission")!!
        Trace.d("permission", "onActivityCreated requestPermission $permission")
        requestPermission(permission)
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

    private fun requestPermission(permission: String?) {
        val denied = ContextCompat.checkSelfPermission(requireContext(), permission!!)
        if (denied == PermissionChecker.PERMISSION_DENIED) {
            requestPermissions(arrayOf<String?>(permission), REQUEST_PERMISSION_CODE)
            return
        }
        permissionGranted(permission)
    }

    private fun permissionGranted(permission: String) {
        Trace.d("permission", "granted:$permission")
        callback?.onPermissionGranted(permission)
        closeSelf()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                permissionGranted(permissions[0])
                return
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
                Trace.d("permission", "onShowRequestPermissionRationale:$permission")
                showRequestPermissionRationale()
            } else {
                Trace.d("permission", "onPermissionDenied:$permission")
                permissionDenied()
            }
        }
    }

    private fun permissionDenied() {
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
        callback?.onPermissionDenied(permission, scope)
    }

    private fun showRequestPermissionRationale() {
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
                                requestPermission(permission)
                            }
                        }.show()
            }
        }
        callback?.onRequestPermissionRationale(permission, scope)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            requestPermission(permission)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnPermissionCallback) {
            callback = parentFragment as OnPermissionCallback?
        } else if (activity is OnPermissionCallback) {
            callback = activity as OnPermissionCallback?
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 20
        fun newInstance(permission: String): PermissionFragment {
            val fragment = PermissionFragment()
            val args = Bundle()
            args.putString("permission", permission)
            fragment.arguments = args
            return fragment
        }
    }
}