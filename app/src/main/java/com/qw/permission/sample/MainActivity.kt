package com.qw.permission.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.qw.permission.*
import com.qw.permission.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).apply {
            bind = this
        }.root)
        notifyDataChanged()
        bind.mLocationBtn.setOnClickListener {
            requestLocation()
        }
        bind.mStorageBtn.setOnClickListener {
            requestStorage()
        }
        bind.mPhoneBtn.setOnClickListener {
            requestCallPhone()
        }
    }

    private fun requestCallPhone(isReasonBeforeRequest: Boolean = true) {
        QPermission.init(this).permission(android.Manifest.permission.CALL_PHONE)
            .setOnResultListener(object : OnResultListener {

                override fun onGranted(permissions: ArrayList<String>) {
                    Toast.makeText(this@MainActivity, "call phone", Toast.LENGTH_LONG).show()
                    notifyDataChanged()
                }

                override fun onDeniedAndNeverAskAgain(deniedPermissions: ArrayList<String>) {
                    AlertDialog.Builder(this@MainActivity).setTitle("电话权限被拒绝")
                        .setMessage("需要打开电话权限->权限设置->电话权限—>打开")
                        .setCancelable(false).setNegativeButton("取消", null)
                        .setPositiveButton("去设置") { dialog, which ->
                            QPermission.settings(this@MainActivity, 100)
                        }.show()
                }

                override fun onShowRequestPermissionRationale(deniedPermissions: ArrayList<String>) {
                    AlertDialog.Builder(this@MainActivity).setTitle("打电话提示")
                        .setMessage("需要先申请拨打电话权限，才可以用哦").setCancelable(false)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("现在申请") { dialog, which ->
                            requestCallPhone(false)
                        }.show()
                }
            }).request(isReasonBeforeRequest)
    }

    private fun requestStorage(isReasonBeforeRequest: Boolean = true) {
        QPermission.init(this)
            .permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setOnResultListener(object : OnResultListener {

                override fun onGranted(permissions: ArrayList<String>) {
                    Toast.makeText(this@MainActivity, "访问sd card", Toast.LENGTH_LONG).show()
                    notifyDataChanged()
                }

                override fun onDeniedAndNeverAskAgain(deniedPermissions: ArrayList<String>) {
                    AlertDialog.Builder(this@MainActivity).setTitle("存储权限被拒绝")
                        .setMessage("需要打开存储权限->权限设置->存储权限—>打开")
                        .setCancelable(false).setNegativeButton("取消", null)
                        .setPositiveButton("去设置") { dialog, which ->
                            QPermission.settings(this@MainActivity, 100)
                        }.show()
                }

                override fun onShowRequestPermissionRationale(deniedPermissions: ArrayList<String>) {
                    AlertDialog.Builder(this@MainActivity).setTitle("打电话提示")
                        .setMessage("需要先申请存储权限，才可以用哦").setCancelable(false)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("现在申请") { dialog, which ->
                            requestStorage(false)
                        }.show()
                }
            }).request(isReasonBeforeRequest)
    }

    private fun requestLocation(isReasonBeforeRequest: Boolean = true) {
        //初始化PermissionHandler实例
        QPermission.init(this)
            //配置申请的权限
            .permission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            //监听申请结果
            .setOnResultListener(object : OnResultListener {
                override fun onGranted(permissions: ArrayList<String>) {
                    //权限申请通过调用
                    location()
                    notifyDataChanged()
                }

                override fun onDeniedAndNeverAskAgain(deniedPermissions: ArrayList<String>) {
                    //权限被永久拒绝调用
                    AlertDialog.Builder(this@MainActivity).setTitle("定位权限被拒绝")
                        .setMessage("需要打开定位权限->权限设置->定位权限—>打开")
                        .setCancelable(false).setNegativeButton("取消", null)
                        .setPositiveButton("去设置") { dialog, which ->
                            QPermission.settings(this@MainActivity, 100)
                        }.show()
                }

                override fun onShowRequestPermissionRationale(deniedPermissions: ArrayList<String>) {
                    //显示申请权限理由
                    AlertDialog.Builder(this@MainActivity).setTitle("定位提示")
                        .setMessage("需要先申请定位权限，才可以用哦").setCancelable(false)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("现在申请") { dialog, which ->
                            requestStorage(false)
                        }.show()
                }
                //isReasonBeforeRequest 如果为true 权限为申请状态下会走onShowRequestPermissionRationale 回调
            }).request(isReasonBeforeRequest)
    }

    private fun location() {
        Toast.makeText(this, "定位中", Toast.LENGTH_SHORT).show()
    }

    private fun notifyDataChanged() {
        val locationGrant =
            QPermission.isGrant(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val storageGrant =
            QPermission.isGrant(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val phoneGrant = QPermission.isGrant(this, android.Manifest.permission.CALL_PHONE)
        bind.mLocationBtn.text = "定位Grant(${locationGrant})"
        bind.mStorageBtn.text = "存储Grant(${storageGrant})"
        bind.mPhoneBtn.text = "电话Grant(${phoneGrant})"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        notifyDataChanged()
    }
}