// Flutter imports:
import 'package:flutter/material.dart';

// Project imports:
import 'package:module_flutter/component/image/asset_image.dart';

Widget backTop(bool showBackTop, ScrollController controller) {
  return Visibility(
    visible: showBackTop,
    child: SizedBox(
      width: 48,
      height: 48,
      child: FloatingActionButton(
        onPressed: () => controller.animateTo(0, duration: const Duration(milliseconds: 200), curve: Curves.linear),
        backgroundColor: Colors.white,
        child: assetImage('images/ic_back_top.png', 28, 28),
      ),
    ),
  );
}
