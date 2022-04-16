package com.qw.permission.sample

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

        bind.mLocationBtn.setOnClickListener {
            val isGrant =
                Permission.isGrant(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            if (isGrant) {
                location()
                return@setOnClickListener
            }
            val shouldShowRequestPermissionRationale =
                Permission.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            if (shouldShowRequestPermissionRationale) {
                AlertDialog.Builder(this)
                    .setTitle("定位权限申请")
                    .setMessage("需要定位权限")
                    .setCancelable(false)
                    .setNegativeButton("取消") { dialog, which ->
                    }
                    .setPositiveButton("确定") { dialog, which ->
                        requestLocation()
                    }.show()
            } else {
                //权限拒绝且不在询问
                requestLocation()
            }
        }
        bind.mStorageBtn.setOnClickListener {
            Permission.init(this)
                .permissions(
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                .setOnRequestPermissionsResultListener(object : OnRequestPermissionsResultListener {
                    override fun onRequestPermissionsResult(result: PermissionResult) {
                        if (result.isGrant()) {
                            Toast.makeText(this@MainActivity, "已授权", Toast.LENGTH_LONG).show()
                        } else {
                            //
                            Toast.makeText(
                                this@MainActivity,
                                "未授权：" + result.getDeniedPermissions(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        notifyDataChanged()
                    }
                })
                .request()
        }
        bind.mPhoneBtn.setOnClickListener {
            Permission.init(this)
                .permissions(
                    arrayOf(
                        android.Manifest.permission.CALL_PHONE
                    )
                )
                .setOnRequestPermissionsResultListener(object : OnRequestPermissionsResultListener {
                    override fun onRequestPermissionsResult(result: PermissionResult) {
                        if (result.isGrant()) {
                            Toast.makeText(this@MainActivity, "已授权", Toast.LENGTH_LONG).show()
                        } else {
                            //
                            Toast.makeText(
                                this@MainActivity,
                                "未授权：" + result.getDeniedPermissions(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        notifyDataChanged()
                    }
                })
                .request()
        }
        notifyDataChanged()

    }

    private fun requestLocation() {
        Permission.init(this)
            .permissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            .setOnRequestPermissionsResultListener(object :
                OnRequestPermissionsResultListener {
                override fun onRequestPermissionsResult(result: PermissionResult) {
                    if (result.isGrant()) {
                        location()
                        Toast.makeText(this@MainActivity, "已授权", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "未授权：" + result.getDeniedPermissions(),
                            Toast.LENGTH_LONG
                        ).show()
                        val shouldShowRequestPermissionRationale =
                            Permission.shouldShowRequestPermissionRationale(
                                this@MainActivity,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        if(!shouldShowRequestPermissionRationale){
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("定位权限提示")
                                .setMessage("需要定位权限")
                                .setCancelable(false)
                                .setNegativeButton("取消") { dialog, which ->
                                }
                                .setPositiveButton("确定") { dialog, which ->
                                    Permission.permissionSettings(this@MainActivity, 100)
                                }.show()
                        }
                    }
                    notifyDataChanged()
                }
            })
            .request()
    }

    private fun location() {
    }

    private fun notifyDataChanged() {
        val locationGrant =
            Permission.isGrant(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val storageGrant =
            Permission.isGrant(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val phoneGrant =
            Permission.isGrant(this, android.Manifest.permission.CALL_PHONE)
        bind.mLocationBtn.text = "定位Grant(${locationGrant})"
        bind.mStorageBtn.text = "存储Grant(${storageGrant})"
        bind.mPhoneBtn.text = "电话Grant(${phoneGrant})"
    }
}