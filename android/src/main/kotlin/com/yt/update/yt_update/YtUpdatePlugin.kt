package com.yt.update.yt_update

import android.app.Activity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel


/** YtUpdatePlugin */
class YtUpdatePlugin : FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel

    private var currentActivity: Activity? = null

    /**   FlutterPlugin*/
    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "yt_update")
        channel.setMethodCallHandler(this)
    }


    private fun dealGoogleUpdate() {
        if (currentActivity == null) return
        GoogleUpdate().update(currentActivity!!, currentActivity!!.packageName)
    }

    private fun dealAppUpdate(apkUrl: String) {
        if (currentActivity == null) return
        AppUpdate.getInstance().update(currentActivity!!, apkUrl)
    }

    private fun dealAppUpdateStatus(result: MethodChannel.Result) {
        AppUpdate.getInstance().apply {
            result.success(
                mapOf(
                    "maxSize" to maxSize,
                    "downSize" to downSize,
                    "downStatus" to downStatus,
                    "errMsg" to errMsg
                )
            )
        }
    }


    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    /**  MethodCallHandler */
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
            "updateGoogle" -> {
                dealGoogleUpdate()
                result.success("")
            }
            "updateApp" -> {
                dealAppUpdate(call.arguments.toString())
                result.success("")
            }
            "updateAppStatus" -> dealAppUpdateStatus(result)
            "updateAppCancel" -> {
                AppUpdate.getInstance().cancelUpdate()
                result.success("")
            }
            else -> result.notImplemented()
        }
    }

    /**   ActivityAware    */
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        currentActivity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        currentActivity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        currentActivity = binding.activity
    }

    override fun onDetachedFromActivity() {
        currentActivity = null
    }

}
