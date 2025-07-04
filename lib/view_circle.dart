import 'dart:math';
import 'package:flutter/material.dart';

class CircleProgress extends StatelessWidget {
  final double value;
  final double strokeWidth;
  final double size;

  final Color progressColor;
  final Color bgColor;

  const CircleProgress(
      {super.key,
      required this.value,
      this.strokeWidth = 8.0,
      this.size = 50,
      this.progressColor = Colors.blue,
      this.bgColor = Colors.transparent});

  @override
  Widget build(BuildContext context) => SizedBox(
      width: size,
      height: size,
      child: CustomPaint(
          size: Size.fromRadius(size),
          painter: _CircleProgressPainter(
              value: value, strokeWidth: strokeWidth, bgColor: bgColor, progressColor: progressColor)));
}

class _CircleProgressPainter extends CustomPainter {
  final double value;
  final double strokeWidth;
  final Color bgColor;
  final Color progressColor;

  final circlePaint = Paint()
    ..strokeCap = StrokeCap.round
    ..style = PaintingStyle.stroke;

  final paintBg = Paint()..style = PaintingStyle.stroke;

  _CircleProgressPainter(
      {required this.value, required this.strokeWidth, required this.bgColor, required this.progressColor}) {
    circlePaint
      ..strokeWidth = strokeWidth
      ..color = progressColor;
    paintBg
      ..strokeWidth = strokeWidth
      ..color = bgColor;
  }

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final radius = size.width / 2 - strokeWidth / 2;
    final rect = Rect.fromCircle(center: center, radius: radius);

    canvas.drawCircle(center, radius, paintBg); // 绘制背景圆

    print("value:$value");
    canvas.drawArc(rect, -pi * 1.5, 2 * pi * value, false, circlePaint); // 绘制进度圆弧
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => true;
}
