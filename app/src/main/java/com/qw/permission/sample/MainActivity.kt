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

    private fun requestCallPhone() {
        Permission.init(this)
            .permission(android.Manifest.permission.CALL_PHONE)
            .setOnPermissionsResultListener(object : OnPermissionsResultListener {
                override fun onShowRequestPermissionRationale(permission: String) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("打电话提示")
                        .setMessage("需要拨打电话权限")
                        .setCancelable(false)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("设置") { dialog, which ->
                            Permission.settings(this@MainActivity, 100)
                        }.show()
                }

                override fun onRequestPermissionsResult(result: PermissionResult) {
                    if (result.isGrant()) {
                        Toast.makeText(this@MainActivity, "call phone", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "未授权：" + result.getDeniedPermissions(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    notifyDataChanged()
                }
            }).request()
    }

    private fun requestStorage() {
        Permission.init(this)
            .permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setOnPermissionsResultListener(object : OnPermissionsResultListener {

                override fun onShowRequestPermissionRationale(permission: String) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("存储提示")
                        .setMessage("需要存储权限")
                        .setCancelable(false)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("设置") { dialog, which ->
                            Permission.settings(this@MainActivity, 100)
                        }.show()
                }

                override fun onRequestPermissionsResult(result: PermissionResult) {
                    if (result.isGrant()) {
                        Toast.makeText(this@MainActivity, "访问sd card", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "未授权：" + result.getDeniedPermissions(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    notifyDataChanged()
                }
            }).request()
    }

    private fun requestLocation() {
        Permission.init(this)
            .permission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            .setOnPermissionsResultListener(object : OnPermissionsResultListener {
                override fun onShowRequestPermissionRationale(permission: String) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("定位提示")
                        .setMessage("需要定位权限")
                        .setCancelable(false)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("设置") { dialog, which ->
                            Permission.settings(this@MainActivity, 100)
                        }.show()
                }

                override fun onRequestPermissionsResult(result: PermissionResult) {
                    if (result.isGrant()) {
                        location()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "未授权：" + result.getDeniedPermissions(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    notifyDataChanged()
                }
            }).request()
    }

    private fun location() {
        Toast.makeText(this, "定位中", Toast.LENGTH_SHORT).show()
    }

    private fun notifyDataChanged() {
        val locationGrant =
            Permission.isGrant(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val storageGrant =
            Permission.isGrant(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val phoneGrant =
            Permission.isGrant(this, android.Manifest.permission.CALL_PHONE)
        bind.mLocationBtn.text = "定位Gra" +
                "nt(${locationGrant})"
        bind.mStorageBtn.text = "存储Grant(${storageGrant})"
        bind.mPhoneBtn.text = "电话Grant(${phoneGrant})"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        notifyDataChanged()
    }
}