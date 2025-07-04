import 'package:flutter/material.dart';
import 'package:yt_update/view_circle.dart';
import 'package:yt_update/yt_update.dart';

class TestView extends StatefulWidget {
  const TestView({super.key});

  @override
  State<StatefulWidget> createState() => _TestViewState();
}

class _TestViewState extends State<TestView> {
  final _ytUpdatePlugin = YtUpdate();

  String statusMsg = "-----";
  double circleValue = 0;

  @override
  Widget build(BuildContext context) => SizedBox(
      width: double.infinity,
      child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          mainAxisSize: MainAxisSize.max,
          children: [
            Expanded(
                child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    mainAxisSize: MainAxisSize.max,
                    children: [CircleProgress(value: circleValue, size: 200, bgColor: Colors.black12)])),
            Expanded(
                child: Column(children: [
              ElevatedButton(onPressed: _tapGoogle, child: const Text("googleUpdate")),
              const SizedBox(height: 20),
              ElevatedButton(onPressed: _tapApp, child: const Text("AppUpdate")),
              const SizedBox(height: 20),
              ElevatedButton(onPressed: _tapCancel, child: const Text("cancel")),
            ]))
          ]));

  void _tapGoogle() => _ytUpdatePlugin.updateGoogle();

  void _tapCancel() => _ytUpdatePlugin.cancel();

  void _tapApp() {
    _ytUpdatePlugin.update(
        apkUrl: "http://1.95.128.88:9999/minio/sys/app/Smart_M_1.5.2_1748243119.apk",
        listener: (status) {
          setState(() {
            if (status.maxSize != 0) circleValue = status.downSize / status.maxSize;
            statusMsg = status.toString();
          });
        });
  }
}
