package com.qw.permission.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.qw.permission.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.mLocationBtn).setOnClickListener {
            requestLocation()
            QPermission.init(this)
                .permissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CALL_PHONE
                    )
                )
                .setOnPermissionDenied(object : OnPermissionDeniedListener {
                    override fun onPermissionDenied(
                        grantedPermissions: Array<String>,
                        scope: PermissionScope
                    ) {
                        scope.title = "权限申请"
                        scope.message = "权限设置"
                        scope.leftText = "取消"
                        scope.rightText = "申请"
                        scope.show()
                    }
                })
                .setOnPermissionRationale(object : OnPermissionRationaleListener {
                    override fun onPermissionRationale(
                        permissions: Array<String>,
                        scope: PermissionScope
                    ) {
                        scope.title = "权限申请"
                        scope.message = "需要申请权限，然后才能继续使用"
                        scope.leftText = "取消"
                        scope.rightText = "申请"
                        scope.show()
                    }
                })
                .setOnPermissionGranted(object : OnPermissionGrantedListener {
                    override fun onPermissionGranted(permissions: Array<String>) {
                        Toast.makeText(
                            this@MainActivity,
                            "onPermissionGranted:$permissions",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }).request()
        }
    }

    private fun requestLocation() {

    }
}