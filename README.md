# QPermission



QPermission是一个android权限管理的工具，api易上手可快速解决权限申请的需求

## 1、如何使用



### 1.1、导入依赖

**Step 1.** Add it in your root build.gradle at the end of repositories

```groovy
allprojects {
	repositories {
			...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency

```groovy
dependencies {
  implementation 'com.github.qinweiforandroid:QPermission:1.0.0416'
}
```

### 1.2、API使用

以申请定位权限为例（android.Manifest.permission.ACCESS_COARSE_LOCATION）

```kotlin
private fun requestLocation() {
    Permission.init(this)
  			//可同时申请多个权限
        .permissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION) )
        .setOnRequestPermissionsResultListener(object :OnRequestPermissionsResultListener {
            override fun onRequestPermissionsResult(result: PermissionResult) {
              	//isGrant()用于判断是否已经授权
                if (result.isGrant()) {
                    //todo 继续做定位的任务
                } else {
                    val shouldShowRequestPermissionRationale =
                        Permission.shouldShowRequestPermissionRationale(
                            this@MainActivity,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    if(!shouldShowRequestPermissionRationale){
                        //todo 用户拒绝权限（不在允许）
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
            }
        })
        .request()
}
```

链式的api调用清晰明了

对接中如遇到问题可以联系QQ：435231045 注意备注QPermission使用问题