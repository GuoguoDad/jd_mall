import 'package:flutter/cupertino.dart';

import 'package:module_flutter/view/page/order/generate_order.dart';

enum PageRouteEnum {
  generateOrder("/generateOrder");

  const PageRouteEnum(this.path);

  final String path;
}

Map<String, WidgetBuilder> routesMap = {
  PageRouteEnum.generateOrder.path: (context) => const GenerateOrder(),
};

Map<String, Widget> widgetsMap = {
  PageRouteEnum.generateOrder.path: const GenerateOrder(),
};
