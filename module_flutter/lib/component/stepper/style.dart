// Flutter imports:
import 'package:flutter/material.dart';

/// Theme extension. 主题扩展，可以在主题中统一设置组件样式
class StepperTheme extends ThemeExtension<StepperTheme> {
  final StepperStyle? style;

  StepperTheme({this.style});

  static StepperStyle of(BuildContext context) {
    final theme = Theme.of(context);
    return theme.extension<StepperTheme>()?.style ?? StepperStyle.fromColorScheme(theme.colorScheme);
  }

  @override
  ThemeExtension<StepperTheme> copyWith({
    StepperStyle? style,
  }) {
    return StepperTheme(style: style ?? this.style?.copyWith());
  }

  @override
  ThemeExtension<StepperTheme> lerp(ThemeExtension<StepperTheme>? other, double t) {
    if (other == null) return this;
    return t > 0.5 ? other : this;
  }
}

/// CartStepper Style. 组件样式
class StepperStyle {
  /// Actived foreground color 激活状态的前景色(文字颜色)
  final Color activeForegroundColor;

  /// Actived background color 激活状态的背景色
  final Color activeBackgroundColor;

  /// Deactived foreground color 未激活状态的前景色(文字颜色)
  final Color foregroundColor;

  /// Deactived background color 未激活状态的背景色
  final Color backgroundColor;

  /// widget shape. 组件形状
  final BoxShape shape;

  /// Border radius of shape. 形状中的圆角值
  final Radius? radius;

  /// Add a border around the shape use AnimatedContainer.
  /// recommanded with out elevation.
  final BoxBorder? border;

  /// shadow color 阴影颜色
  final Color? shadowColor;

  /// Value text style 显示值的字体样式
  final TextStyle? textStyle;

  /// Icon theme
  final IconThemeData iconTheme;

  /// Add button icon
  final IconData? iconPlus;

  /// Subtract button icon
  final IconData? iconMinus;

  /// Button aspectRatio
  final double buttonAspectRatio;

  /// Widget elevation
  final double elevation;

  const StepperStyle({
    this.activeForegroundColor = Colors.white,
    this.activeBackgroundColor = Colors.blue,
    this.foregroundColor = Colors.black54,
    this.backgroundColor = Colors.white,
    this.shape = BoxShape.rectangle,
    this.radius,
    this.border,
    this.shadowColor,
    this.textStyle,
    this.iconTheme = const IconThemeData(),
    this.iconPlus,
    this.iconMinus,
    this.buttonAspectRatio = 1,
    this.elevation = 2,
  });

  /// Create style from theme
  factory StepperStyle.fromTheme(
    ThemeData theme, {
    BoxShape shape = BoxShape.rectangle,
    Radius? radius,
    BoxBorder? border,
    IconThemeData? iconTheme,
    IconData? iconPlus,
    IconData? iconMinus,
    double? buttonAspectRatio,
    double? elevation,
  }) {
    return StepperStyle.fromColorScheme(
      theme.colorScheme,
      shape: shape,
      radius: radius,
      border: border,
      textStyle: theme.textTheme.bodyMedium,
      iconPlus: iconPlus,
      iconMinus: iconMinus,
      buttonAspectRatio: buttonAspectRatio,
      elevation: elevation,
    );
  }

  /// Create style from colorScheme
  factory StepperStyle.fromColorScheme(
    ColorScheme colorScheme, {
    BoxShape shape = BoxShape.rectangle,
    Radius? radius,
    BoxBorder? border,
    TextStyle? textStyle,
    IconThemeData? iconTheme,
    IconData? iconPlus,
    IconData? iconMinus,
    double? buttonAspectRatio,
    double? elevation,
  }) {
    return StepperStyle(
      activeForegroundColor: colorScheme.onPrimary,
      activeBackgroundColor: colorScheme.primary,
      foregroundColor: colorScheme.onSurface,
      backgroundColor: colorScheme.surface,
      shadowColor: colorScheme.shadow,
      shape: shape,
      radius: radius,
      border: border,
      textStyle: textStyle,
      iconTheme: iconTheme ?? const IconThemeData(),
      iconPlus: iconPlus,
      iconMinus: iconMinus,
      elevation: elevation ?? 2,
    );
  }

  /// copyWidth
  StepperStyle copyWith({
    Color? activeForegroundColor,
    Color? activeBackgroundColor,
    Color? foregroundColor,
    Color? backgroundColor,
    BoxShape? shape,
    Radius? radius,
    BoxBorder? border,
    bool noBorder = false,
    Color? shadowColor,
    TextStyle? textStyle,
    IconThemeData? iconTheme,
    IconData? iconPlus,
    IconData? iconMinus,
    double? buttonAspectRatio,
    double? elevation,
  }) {
    return StepperStyle(
      activeForegroundColor: activeForegroundColor ?? this.activeForegroundColor,
      activeBackgroundColor: activeBackgroundColor ?? this.activeBackgroundColor,
      foregroundColor: foregroundColor ?? this.foregroundColor,
      backgroundColor: backgroundColor ?? this.backgroundColor,
      shape: shape ?? this.shape,
      radius: radius ?? this.radius,
      border: noBorder ? null : (border ?? this.border),
      shadowColor: shadowColor ?? this.shadowColor,
      textStyle: textStyle ?? this.textStyle,
      iconTheme: iconTheme ?? this.iconTheme,
      iconPlus: iconPlus ?? this.iconPlus,
      iconMinus: iconMinus ?? this.iconMinus,
      buttonAspectRatio: buttonAspectRatio ?? this.buttonAspectRatio,
      elevation: elevation ?? this.elevation,
    );
  }
}
