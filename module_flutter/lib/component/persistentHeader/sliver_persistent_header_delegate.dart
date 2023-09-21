// Flutter imports:
import 'package:flutter/cupertino.dart';
import 'package:flutter/rendering.dart';

abstract class SliverPersistentHeaderDelegate {

  // header 最大高度；pined为 true 时，当 header 刚刚固定到顶部时高度为最大高度。
  double get maxExtent;

  // header 的最小高度；pined为true时，当header固定到顶部，用户继续往上滑动时，header
  // 的高度会随着用户继续上滑从 maxExtent 逐渐减小到 minExtent
  double get minExtent;

  // 构建 header。
  // shrinkOffset取值范围[0,maxExtent],当header刚刚到达顶部时，shrinkOffset 值为0，
  // 如果用户继续向上滑动列表，shrinkOffset的值会随着用户滑动的偏移减小，直到减到0时。
  //
  // overlapsContent：一般不建议使用，在使用时一定要小心，后面会解释。
  Widget build(BuildContext context, double shrinkOffset, bool overlapsContent);

  // header 是否需要重新构建；通常当父级的 StatefulWidget 更新状态时会触发。
  // 一般来说只有当 Delegate 的配置发生变化时，应该返回false，比如新旧的 minExtent、maxExtent
  // 等其他配置不同时需要返回 true，其余情况返回 false 即可。
  bool shouldRebuild(covariant SliverPersistentHeaderDelegate oldDelegate);

  // 下面这几个属性是SliverPersistentHeader在SliverAppBar中时实现floating、snap
  // 效果时会用到，平时开发过程很少使用到，读者可以先不用理会。
  TickerProvider? get vsync => null;
  FloatingHeaderSnapConfiguration? get snapConfiguration => null;
  OverScrollHeaderStretchConfiguration? get stretchConfiguration => null;
  PersistentHeaderShowOnScreenConfiguration? get showOnScreenConfiguration => null;

}
