import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:yt_update/bean_status.dart';

class YtUpdatePlatform extends PlatformInterface {
  YtUpdatePlatform() : super(token: _token);

  static final Object _token = Object();

  static YtUpdatePlatform _instance = YtUpdatePlatform();

  static YtUpdatePlatform get instance => _instance;

  static set instance(YtUpdatePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  @visibleForTesting
  final methodChannel = const MethodChannel('yt_update');

  Future<String?> getPlatformVersion() async => await methodChannel.invokeMethod<String>('getPlatformVersion');

  Future<void> updateGoogle() async => await methodChannel.invokeMethod("updateGoogle");

  Future<void> updateApp({required String apkUrl}) async => await methodChannel.invokeMethod("updateApp", apkUrl);

  Future<AppUpdateStatusBean> updateAppStatus() async {
    Map bbb = await methodChannel.invokeMethod("updateAppStatus");
    print("updateAppStatus:$bbb");
    return AppUpdateStatusBean(bbb);
  }

  Future<void> updateInstall() async => await methodChannel.invokeMethod("updateInstall");

  Future<void> updateAppCancel() async => await methodChannel.invokeMethod("updateAppCancel");
}
