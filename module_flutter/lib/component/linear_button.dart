// Flutter imports:
import 'package:flutter/material.dart';

// Project imports:
import 'package:module_flutter/common/style/common_style.dart';

const defaultColors = [Color(0xFFDE2F21), Color(0xFFEC592F)];

class LinearButton extends StatefulWidget {
  final double width;
  final double height;
  final String btnName;
  final Color? highlightColor;
  final AlignmentGeometry linearBegin;
  final AlignmentGeometry linearEnd;
  final List<Color>? colors;
  final BorderRadius? borderRadius;
  final GestureTapCallback? onTap;

  const LinearButton({
    super.key,
    required this.width,
    required this.height,
    required this.btnName,
    this.highlightColor,
    this.linearBegin = Alignment.topLeft,
    this.linearEnd = Alignment.bottomRight,
    this.colors,
    this.borderRadius,
    this.onTap,
  });

  @override
  State<StatefulWidget> createState() => _LinearButtonState();
}

class _LinearButtonState extends State<LinearButton> {
  @override
  Widget build(BuildContext context) {
    BorderRadius bRadius = widget.borderRadius ?? const BorderRadius.all(Radius.circular(20));

    return Material(
      child: Ink(
        width: widget.width,
        height: widget.height,
        decoration: BoxDecoration(
          gradient: LinearGradient(begin: widget.linearBegin, end: widget.linearEnd, colors: widget.colors ?? defaultColors),
          borderRadius: bRadius,
        ),
        child: InkWell(
          borderRadius: bRadius,
          highlightColor: widget.highlightColor ?? CommonStyle.themeColor,
          onTap: widget.onTap,
          child: Container(
            alignment: Alignment.center,
            child: Text(
              widget.btnName,
              style: const TextStyle(color: Colors.white, fontSize: 14, fontWeight: FontWeight.bold),
            ),
          ),
        ),
      ),
    );
  }
}
