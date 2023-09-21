// Flutter imports:
import 'package:flutter/material.dart';

typedef PageIndicatorItemBuilder = Widget Function(bool isSelected, Size itemSize);

/// 轮播图指示器
class PageIndicator extends StatelessWidget {
  PageIndicator({
    Key? key,
    this.margin = const EdgeInsets.only(bottom: 10),
    required this.currentPage,
    required this.itemCount,
    this.normalColor = const Color(0x25ffffff),
    this.selectedColor = Colors.white,
    this.itemSize = const Size(8, 2),
    this.itemBuilder,
    this.hidesForSinglePage = true,
  }) : super(key: key);

  /// 当前页面索引
  ValueNotifier<int> currentPage;

  EdgeInsetsGeometry? margin;

  /// item数量
  int itemCount;

  /// 每个item尺寸(最好用固定宽度除以个数,避免总宽度溢出)
  Size itemSize;

  /// 自定义每个item
  PageIndicatorItemBuilder? itemBuilder;

  /// 默认颜色
  Color? normalColor;

  /// 选中颜色
  Color? selectedColor;

  /// 单页隐藏
  bool hidesForSinglePage;

  @override
  Widget build(BuildContext context) {
    if (hidesForSinglePage && itemCount == 1) {
      return const SizedBox();
    }
    return buildPageIndicator();
  }

  Widget buildPageIndicator() {
    return Container(
      margin: margin,
      child: Align(
        alignment: Alignment.bottomCenter,
        child: ValueListenableBuilder(
          valueListenable: currentPage,
          builder: (BuildContext context, dynamic value, Widget? child) {
            return Row(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.start,
              children: buildPageIndicatorItem(
                currentIndex: currentPage.value,
                normalColor: normalColor,
                selectedColor: selectedColor,
              ),
            );
          },
        ),
      ),
    );
  }

  List<Widget> buildPageIndicatorItem({
    currentIndex = 0,
    normalColor = const Color(0x25ffffff),
    selectedColor = Colors.white,
  }) {
    return List.generate(itemCount, (index) {
      return itemBuilder != null
          ? itemBuilder!(currentIndex == index, itemSize)
          : ClipRRect(
              borderRadius: const BorderRadius.all(Radius.circular(1)),
              child: Container(
                width: itemSize.width,
                height: itemSize.height,
                color: currentIndex == index ? selectedColor : normalColor,
              ),
            );
    });
  }
}
