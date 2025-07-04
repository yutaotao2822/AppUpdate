package com.yt.update.yt_update.update.base.bean

import java.io.File


/**
 * createDate: 2022/4/14 on 11:18
 * desc:
 *
 * @author azhon
 */

sealed class DownloadStatus {

    object Start : DownloadStatus()

    data class Downloading(val max: Int, val progress: Int) : DownloadStatus()

    class Done(val apk: File) : DownloadStatus()

    object Cancel : DownloadStatus()

    data class Error(val e: Throwable) : DownloadStatus()
}
