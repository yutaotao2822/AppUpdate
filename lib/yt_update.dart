import 'dart:io';

import 'package:url_launcher/url_launcher.dart';
import 'package:url_launcher/url_launcher_string.dart';
import 'package:yt_update/bean_status.dart';

import 'yt_update_platform_interface.dart';

class YtUpdate {
  Future<String?> getPlatformVersion() => YtUpdatePlatform.instance.getPlatformVersion();

  Future<void> updateGoogle() async => await YtUpdatePlatform.instance.updateGoogle();

  bool isRun = false;

  Future<void> update({required String apkUrl, required Function(AppUpdateStatusBean status) listener}) async {
    if (Platform.isIOS) {
      if (await canLaunchUrl(Uri.parse(apkUrl))) {
        await launchUrlString(apkUrl);
      } else {
        listener(AppUpdateStatusBean({"downStatus": 4, "errMsg": 'Could not launch $apkUrl'}));
      }
    } else {
      isRun = true;
      await YtUpdatePlatform.instance.updateApp(apkUrl: apkUrl);
      _updateStatus(listener);
      return;
    }
  }

  void _updateStatus(Function(AppUpdateStatusBean status) listener) {
    Future.delayed(const Duration(seconds: 1), () async {
      var status = await YtUpdatePlatform.instance.updateAppStatus();
      isRun = [AppUpdateStatus.wait, AppUpdateStatus.start, AppUpdateStatus.download].contains(status.downStatus);
      listener.call(status);
      if (!isRun) return;
      _updateStatus(listener);
    });
  }

  Future<void> cancel() async {
    isRun = false;
    await YtUpdatePlatform.instance.updateAppCancel();
    return;
  }

  Future<void> install() async => await YtUpdatePlatform.instance.updateInstall();

  Future<void> jumpBrowser(String url) async {
    isRun = false;
    await launchUrlString(url);
    return;
  }
}
