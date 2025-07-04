import 'package:flutter/material.dart';
import 'package:yt_update_example/test_widget.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) =>
      MaterialApp(home: Scaffold(appBar: AppBar(title: const Text('Plugin example app')), body: const TestView()));
}
