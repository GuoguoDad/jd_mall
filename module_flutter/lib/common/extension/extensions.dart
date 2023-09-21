// Flutter imports:
import 'package:flutter/material.dart';

extension ColorExtension on String {
  toColor() {
    var hexString = this;
    final buffer = StringBuffer();
    if (hexString.length == 6 || hexString.length == 7) buffer.write('ff');
    buffer.write(hexString.replaceFirst('#', ''));

    final hexNum = int.parse(buffer.toString(), radix: 16);
    if (hexNum == 0) {
      return const Color(0xff000000);
    }

    return Color(hexNum);
  }
}
