import 'package:flutter_test/flutter_test.dart';
import 'package:yt_update/yt_update.dart';
import 'package:yt_update/yt_update_platform_interface.dart';


void main() {
  final YtUpdatePlatform initialPlatform = YtUpdatePlatform.instance;

  test('$YtUpdatePlatform is the default instance', () {
    expect(initialPlatform, isInstanceOf<YtUpdatePlatform>());
  });

  test('getPlatformVersion', () async {
    YtUpdate ytUpdatePlugin = YtUpdate();
    YtUpdatePlatform fakePlatform = YtUpdatePlatform();
    YtUpdatePlatform.instance = fakePlatform;

    expect(await ytUpdatePlugin.getPlatformVersion(), '42');
  });
}
