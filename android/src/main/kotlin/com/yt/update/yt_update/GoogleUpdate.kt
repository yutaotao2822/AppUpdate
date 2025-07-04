package com.yt.update.yt_update

import android.content.Context
import android.content.Intent
import android.net.Uri

class GoogleUpdate {

    fun update(context: Context, pkg: String) {
        if (isGooglePlayInstalled(context)) {
            googleUpdate(context, pkg)
        } else googleUpdateByWeb(context, pkg)
    }

    // 检查Google Play是否安装
    private fun isGooglePlayInstalled(context: Context): Boolean {
        val uri = Uri.parse("market://search?q=pname:com.google.android.gms")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        return intent.resolveActivity(context.packageManager) != null
    }

    //跳转到google更新
    private fun googleUpdate(context: Context, pkg: String) {
        if (pkg.isEmpty()) return
        context.startActivity(Intent("android.intent.action.VIEW").apply {
            data = Uri.parse("market://details?id=$pkg")
            setPackage("com.android.vending")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    //跳转到google更新
    private fun googleUpdateByWeb(context: Context, pkg: String) {
        if (pkg.isEmpty()) return
        context.startActivity(Intent("android.intent.action.VIEW").apply {
            data = Uri.parse("https://play.google.com/store/apps/details?id=$pkg")
            setPackage("com.android.vending")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}