// Flutter imports:
import 'package:flutter/material.dart';

Widget textItem({
  required double marginTop,
  required double paddingLeft,
  required double paddingRight,
  Color? bgColor = Colors.transparent,
  Gradient? gradient,
  BorderRadiusGeometry? borderRadius,
  required String txt,
  Color? fColor = Colors.white,
  double? fSize = 14,
}) {
  return Container(
    margin: EdgeInsets.only(top: marginTop, left: 5, right: 5),
    padding: EdgeInsets.only(left: paddingLeft, right: paddingRight),
    decoration: BoxDecoration(
      color: bgColor,
      gradient: gradient,
      borderRadius: borderRadius,
    ),
    child: Text(txt, style: TextStyle(color: fColor, fontSize: fSize)),
  );
}
