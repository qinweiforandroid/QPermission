package com.qw.permission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class PermissionFragment : Fragment() {
    private var mResultListener: OnResultListener? = null
    private lateinit var result: PermissionResult
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            //处理进程在后台被清理场景
            closeSelf()
            return null
        }
        result = requireArguments().getSerializable("permissions")!! as PermissionResult
        //check
        if (result.isGrant()) {
            mResultListener?.onGranted(result.grantPermissions)
            return null
        }
        val launch =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                it.forEach { item ->
                    if (item.value) {
                        result.grantPermissions.add(item.key)
                        result.deniedPermissions.remove(item.key)
                    }
                }
                val deniedAndNeverAskAgain = ArrayList<String>()
                result.deniedPermissions.forEach { denied ->
                    if (!QPermission.shouldShowRequestPermissionRationale(
                            requireActivity(), denied
                        )
                    ) {
                        deniedAndNeverAskAgain.add(denied)
                    }
                }
                if (deniedAndNeverAskAgain.isNotEmpty()) {
                    mResultListener?.onDeniedAndNeverAskAgain(result.deniedPermissions)
                } else {
                    if (result.deniedPermissions.isNotEmpty()) {
                        mResultListener?.onShowRequestPermissionRationale(result.deniedPermissions)
                    } else {
                        mResultListener?.onGranted(result.grantPermissions)
                    }
                }
                closeSelf()
            }
        launch.launch(result.deniedPermissions.toArray(arrayOf()))
        return null
    }

    private fun closeSelf() {
        if (parentFragment == null) {
            requireActivity().supportFragmentManager.beginTransaction().remove(this)
                .commitAllowingStateLoss()
        } else {
            requireParentFragment().childFragmentManager.beginTransaction().remove(this)
                .commitAllowingStateLoss()
        }
    }

    fun setOnResultListener(listener: OnResultListener?) {
        this.mResultListener = listener
    }

    override fun onDetach() {
        super.onDetach()
        this.mResultListener = null
    }


    companion object {
        fun newInstance(permissions: PermissionResult): PermissionFragment {
            val fragment = PermissionFragment()
            fragment.arguments = Bundle().apply {
                putSerializable("permissions", permissions)
            }
            return fragment
        }
    }
}