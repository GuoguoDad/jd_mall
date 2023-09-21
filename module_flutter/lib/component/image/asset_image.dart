// Flutter imports:
import 'package:flutter/cupertino.dart';

Widget assetImage(String path, double w, double h) => Image(image: AssetImage(path), width: w, height: h, fit: BoxFit.cover);

Widget assetFillImage(String path, double w, double h) => Image(image: AssetImage(path), width: w, height: h, fit: BoxFit.fill);
