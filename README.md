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

最新版本[![](https://jitpack.io/v/qinweiforandroid/QPermission.svg)](https://jitpack.io/#qinweiforandroid/QPermission)

```groovy
dependencies {
  implementation 'com.github.qinweiforandroid:QPermission:2.0.0801'
}
```

### 1.2、API使用

以申请定位权限为例（android.Manifest.permission.ACCESS_COARSE_LOCATION）

```kotlin
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
```

链式的api调用清晰明了

对接中如遇到问题可以联系QQ：435231045 注意备注QPermission的使用问题