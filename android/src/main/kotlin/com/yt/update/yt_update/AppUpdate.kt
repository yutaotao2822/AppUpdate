package com.yt.update.yt_update

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.util.Log
import com.yt.update.yt_update.update.config.Constant
import com.yt.update.yt_update.update.listener.OnDownloadListener
import com.yt.update.yt_update.update.manager.DownloadManager
import com.yt.update.yt_update.update.util.ApkUtil
import com.yt.update.yt_update.update.util.NotificationUtil
import java.io.File

class AppUpdate private constructor() {
    companion object {
        private var instance: AppUpdate? = null

        internal fun getInstance(): AppUpdate {
            if (instance == null) instance = AppUpdate()
            return instance!!
        }
    }

    var manager: DownloadManager? = null

    var maxSize: Int = 0//文件大小
    var downSize: Int = 0//下载大小

    //当前状态
    //0未开始 1开始下载 2下载中 3取消下载 4下载异常 5下载完成
    var downStatus: Int = 0

    //下载失败原因
    var errMsg: String? = null


    var apkPath: String = ""

    fun cancelUpdate() = manager?.cancel()

    fun install(act: Activity) {
        try {
            ApkUtil.installApk(act, "${act.packageName}.fileProvider", File(apkPath))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun update(act: Activity, updApkUrl: String) {
        downStatus = 0

        val pkgName = updApkUrl.split("/").last()
        apkPath = "${act.externalCacheDir?.path}/$pkgName"
        ApkUtil.deleteOldApk(act, apkPath)

        manager = DownloadManager.Builder(act).run {
            apkUrl(updApkUrl)
            apkName(pkgName)
            downloadPath = act.externalCacheDir?.path
            smallIcon(R.drawable.app_update_dialog_close)
            showBgdToast(false)
            showNewerToast(false)
            enableLog(true)
            jumpInstallPage(true)
            showNotification(true)
            onDownloadListener(downloadListener)
            build()
        }
        manager?.download()
    }


    private var downloadListener = object : OnDownloadListener {
        override fun start() {
            downStatus = 1
            errMsg = null
        }

        override fun downloading(max: Int, progress: Int) {
            downStatus = 2
            maxSize = max
            downSize = progress
        }

        override fun cancel() {
            downStatus = 3
        }

        override fun error(e: Throwable) {
            downStatus = 4
            errMsg = e.message
        }

        override fun done(apk: File) {
            downStatus = 5
        }
    }

}