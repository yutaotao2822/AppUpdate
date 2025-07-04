package com.yt.update.yt_update.update.base

import com.yt.update.yt_update.update.base.bean.DownloadStatus
import kotlinx.coroutines.flow.Flow

/**
 * createDate: 2022/4/7 on 10:24
 * desc:
 *
 * @author azhon
 */

abstract class BaseHttpDownloadManager {
    /**
     * download apk from apkUrl
     *
     * @param apkUrl
     * @param apkName
     */
    abstract fun download(apkUrl: String, apkName: String): Flow<DownloadStatus>

    /**
     * cancel download apk
     */
    abstract fun cancel()

    /**
     * release memory
     */
    abstract fun release()
}