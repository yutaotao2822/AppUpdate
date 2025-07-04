import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:yt_update/yt_update_platform_interface.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  YtUpdatePlatform platform = YtUpdatePlatform();
  const MethodChannel channel = MethodChannel('yt_update');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
