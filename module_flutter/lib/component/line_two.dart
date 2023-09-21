// Flutter imports:
import 'package:flutter/material.dart';

Widget lineTwo({required String txt, Color? fColor = Colors.white, double? fSize = 14, FontWeight? fontWeight = FontWeight.normal}) {
  return Container(
    padding: const EdgeInsets.only(left: 5, right: 5),
    margin: const EdgeInsets.only(top: 5),
    child: Text(
      txt,
      maxLines: 2,
      overflow: TextOverflow.ellipsis,
      style: TextStyle(color: fColor, fontSize: fSize, fontWeight: fontWeight),
    ),
  );
}
