package com.yt.update.yt_update.update.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.yt.update.yt_update.R
import com.yt.update.yt_update.update.config.Constant
import com.yt.update.yt_update.update.listener.OnButtonClickListener
import com.yt.update.yt_update.update.listener.OnDownloadListenerAdapter
import com.yt.update.yt_update.update.manager.DownloadManager
import com.yt.update.yt_update.update.service.DownloadService
import com.yt.update.yt_update.update.util.ApkUtil
import com.yt.update.yt_update.update.util.DensityUtil
import com.yt.update.yt_update.update.util.LogUtil
import java.io.File


/**
 * createDate:  2022/4/7 on 17:40
 * desc:
 *
 * @author azhon
 */

class UpdateDialogActivity : Activity(), View.OnClickListener {

    private val install = 0x45
    private val error = 0x46
    private val permissionCode = 0x47
    private var manager: DownloadManager? = null
    private lateinit var apk: File
    private lateinit var progressBar: NumberProgressBar
    private lateinit var btnUpdate: Button

    companion object {
        private const val TAG = "UpdateDialogActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        title = ""
        setContentView(R.layout.app_update_dialog_update)
        //system back button
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                backPressed()
//            }
//        })
        init()
    }

    private fun init() {
        manager = DownloadManager.getInstance()
        if (manager == null) {
            LogUtil.e(TAG, "An exception occurred by DownloadManager=null,please check your code!")
            return
        }
        if (manager!!.forcedUpgrade) {
            manager!!.onDownloadListeners.add(listenerAdapter)
        }
        setWindowSize()
        initView(manager!!)
    }

    private fun initView(manager: DownloadManager) {
        val ibClose = findViewById<View>(R.id.ib_close)
        val vLine = findViewById<View>(R.id.line)
        val ivBg = findViewById<ImageView>(R.id.iv_bg)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvSize = findViewById<TextView>(R.id.tv_size)
        val tvDescription = findViewById<TextView>(R.id.tv_description)
        progressBar = findViewById(R.id.np_bar)
        btnUpdate = findViewById(R.id.btn_update)
        progressBar.visibility = if (manager.forcedUpgrade) View.VISIBLE else View.GONE
        btnUpdate.tag = 0
        btnUpdate.setOnClickListener(this)
        ibClose.setOnClickListener(this)
        if (manager.dialogImage != -1) {
            ivBg.setBackgroundResource(manager.dialogImage)
        }
        if (manager.dialogButtonTextColor != -1) {
            btnUpdate.setTextColor(manager.dialogButtonTextColor)
        }
        if (manager.dialogProgressBarColor != -1) {
            progressBar.reachedBarColor = manager.dialogProgressBarColor
            progressBar.setProgressTextColor(manager.dialogProgressBarColor)
        }
        if (manager.dialogButtonColor != -1) {
            val colorDrawable = GradientDrawable().apply {
                setColor(manager.dialogButtonColor)
                cornerRadius = DensityUtil.dip2px(this@UpdateDialogActivity, 3f)
            }
            val drawable = StateListDrawable().apply {
                addState(intArrayOf(android.R.attr.state_pressed), colorDrawable)
                addState(IntArray(0), colorDrawable)
            }
            btnUpdate.background = drawable
        }
        if (manager.forcedUpgrade) {
            vLine.visibility = View.GONE
            ibClose.visibility = View.GONE
        }
        if (manager.apkVersionName.isNotEmpty()) {
            tvTitle.text = String.format(
                resources.getString(R.string.app_update_dialog_new), manager.apkVersionName
            )
        }
        if (manager.apkSize.isNotEmpty()) {
            tvSize.text = String.format(
                resources.getString(R.string.app_update_dialog_new_size), manager.apkSize
            )
            tvSize.visibility = View.VISIBLE
        }
        tvDescription.text = manager.apkDescription
    }

    private fun setWindowSize() {
        val attributes = window.attributes
        attributes.width = DensityUtil.dip2px(this@UpdateDialogActivity, 280f).toInt()
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.gravity = Gravity.CENTER
        window.attributes = attributes
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ib_close -> {
                if (manager?.forcedUpgrade == false) {
                    finish()
                }
                manager?.onButtonClickListener?.onButtonClick(OnButtonClickListener.CANCEL)
            }
            R.id.btn_update -> {
                if (btnUpdate.tag == install) {
                    ApkUtil.installApk(this, Constant.AUTHORITIES!!, apk)
                    return
                }
                if (!checkPermission()) {
                    startUpdate()
                }
            }
        }
    }

    /**
     * check Notification runtime permission [DownloadManager.showNotification] is true && when api>=33.
     * @return false: can continue to download, true: request permission.
     */
    private fun checkPermission(): Boolean {
        if (manager?.showNotification == false) {
            LogUtil.d(TAG, "checkPermission: manager.showNotification = false")
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LogUtil.d(TAG, "checkPermission: has permission")
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            LogUtil.d(TAG, "checkPermission: request permission")
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), permissionCode
            )
            return true
        }
        return false
    }

    private fun startUpdate() {
        if (manager?.forcedUpgrade == true) {
            btnUpdate.isEnabled = false
            btnUpdate.text = resources.getString(R.string.app_update_background_downloading)
        } else {
            finish()
        }
        manager?.onButtonClickListener?.onButtonClick(OnButtonClickListener.UPDATE)
        startService(Intent(this, DownloadService::class.java))
    }

    private fun backPressed() {
        if (manager?.forcedUpgrade == true) return
        finish()
        manager?.onButtonClickListener?.onButtonClick(OnButtonClickListener.CANCEL)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    private val listenerAdapter: OnDownloadListenerAdapter = object : OnDownloadListenerAdapter() {
        override fun start() {
            btnUpdate.isEnabled = false
            btnUpdate.text = resources.getString(R.string.app_update_background_downloading)
        }

        override fun downloading(max: Int, progress: Int) {
            if (max != -1) {
                val curr = (progress / max.toDouble() * 100.0).toInt()
                progressBar.progress = curr
            } else {
                progressBar.visibility = View.GONE
            }
        }

        override fun done(apk: File) {
            this@UpdateDialogActivity.apk = apk
            btnUpdate.tag = install
            btnUpdate.isEnabled = true
            btnUpdate.text = resources.getString(R.string.app_update_click_hint)
        }

        override fun error(e: Throwable) {
            btnUpdate.tag = error
            btnUpdate.isEnabled = true
            btnUpdate.text = resources.getString(R.string.app_update_continue_downloading)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionCode == requestCode) {
            startUpdate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        manager?.onDownloadListeners?.remove(listenerAdapter)
    }
}