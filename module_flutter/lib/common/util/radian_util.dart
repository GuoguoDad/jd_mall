// Dart imports:
import 'dart:math';

/// 角度弧度处理工具类
/// 弧度是角的度量单位 单位缩写是rad  360°角=2π弧度
/// 在Flutter中，π 使用 [pi] 来表示 1弧度约为57.3°，1°为π/180弧度
class RadianUtil {
  //
  ///弧度换算成角度 参数[radian]为弧度
  static double radianToAngle(double radian) {
    return radian * 180 / pi;
  }

  ///角度换算成弧度 参数[angle]为角度
  static double angleToRadian(double angle) {
    return angle * pi / 180;
  }
}
