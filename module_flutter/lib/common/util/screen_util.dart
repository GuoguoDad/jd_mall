// Flutter imports:
import 'package:flutter/material.dart';

double getScreenHeight(BuildContext context) {
  return MediaQuery.sizeOf(context).height;
}

double getScreenWidth(BuildContext context) {
  return MediaQuery.sizeOf(context).width;
}

double getStatusHeight(BuildContext context) {
  return MediaQuery.viewPaddingOf(context).top;
}

double getBottomSpace(BuildContext context) {
  return MediaQuery.viewPaddingOf(context).bottom;
}

double getKeyboardHeight(BuildContext context) {
  return MediaQuery.viewInsetsOf(context).bottom;
}

double getSafeAreaHeight(BuildContext context) {
  return getScreenHeight(context) - getStatusHeight(context) - getBottomSpace(context);
}

/// 获取底部getBottomNavigationBarHeight高度 包含 bottomSpace
double getBottomNavigationBarHeight(BuildContext context) {
  return kBottomNavigationBarHeight + getBottomSpace(context);
}
