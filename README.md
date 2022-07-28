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
  implementation 'com.github.qinweiforandroid:QPermission:1.0.0728'
}
```

### 1.2、API使用

以申请定位权限为例（android.Manifest.permission.ACCESS_COARSE_LOCATION）

```kotlin
private fun requestLocation() {
  //初始化Permission实例
  Permission.init(this)
  //设置需要申请的权限
  .permission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
  //添加权限申请监听器
  .setOnPermissionsResultListener(object : OnPermissionsResultListener {
    override fun onShowRequestPermissionRationale(permission: String) {
      //权限被永久拒绝时调用
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
      //权限申请结果
      if (result.isGrant()) {
        //同意
        location()
      } else {
        //拒绝
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
```

链式的api调用清晰明了

对接中如遇到问题可以联系QQ：435231045 注意备注QPermission的使用问题