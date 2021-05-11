package com.qw.permission

import android.content.DialogInterface


class PermissionScope {
    var title = ""
    var message = ""
    var leftText = ""
    var rightText = ""
    var leftListener: DialogInterface.OnClickListener? = null
    var rightListener: DialogInterface.OnClickListener? = null
    internal var showTask: Runnable? = null
    fun show() {
        showTask?.run()
    }
}