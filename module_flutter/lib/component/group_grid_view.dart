// Flutter imports:
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

class GroupGridView extends StatefulWidget {
  const GroupGridView({
    required this.gridDelegate,
    required this.sectionCount,
    required this.itemInSectionBuilder,
    required this.itemInSectionCount,
    this.headerForSection,
    this.footerForSection,
    this.scrollDirection = Axis.vertical,
    this.reverse = false,
    this.controller,
    this.primary,
    this.physics,
    this.shrinkWrap = false,
    this.padding,
    this.findChildIndexCallback,
    this.addAutomaticKeepAlive = true,
    this.addRepaintBoundaries = true,
    this.addSemanticIndexes = true,
    this.cacheExtent,
    this.dragStartBehavior = DragStartBehavior.start,
    this.keyboardDismissBehavior = ScrollViewKeyboardDismissBehavior.manual,
    this.restorationId,
    this.clipBehavior = Clip.hardEdge,
    super.key,
  });

  final SliverGridDelegate gridDelegate;
  final int sectionCount;
  final Widget Function(BuildContext context, IndexPath indexPath) itemInSectionBuilder;
  final int Function(int section) itemInSectionCount;

  final Widget? Function(int section)? headerForSection;
  final Widget? Function(int section)? footerForSection;

  final Axis scrollDirection;
  final bool reverse;
  final ScrollController? controller;
  final bool? primary;
  final ScrollPhysics? physics;
  final bool shrinkWrap;
  final EdgeInsetsGeometry? padding;
  final IndexPath? Function(Key)? findChildIndexCallback;
  final bool addAutomaticKeepAlive;
  final bool addRepaintBoundaries;
  final bool addSemanticIndexes;
  final double? cacheExtent;
  final DragStartBehavior dragStartBehavior;
  final ScrollViewKeyboardDismissBehavior keyboardDismissBehavior;
  final String? restorationId;
  final Clip clipBehavior;

  @override
  State<StatefulWidget> createState() => _GroupGridViewState();
}

class _GroupGridViewState extends State<GroupGridView> {
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: widget.padding ?? EdgeInsets.zero,
      child: CustomScrollView(
        scrollDirection: widget.scrollDirection,
        reverse: widget.reverse,
        controller: widget.controller,
        primary: widget.primary,
        physics: widget.physics,
        shrinkWrap: widget.shrinkWrap,
        cacheExtent: widget.cacheExtent,
        dragStartBehavior: widget.dragStartBehavior,
        keyboardDismissBehavior: widget.keyboardDismissBehavior,
        restorationId: widget.restorationId,
        clipBehavior: widget.clipBehavior,
        slivers: _listSliver,
      ),
    );
  }

  List<Widget> get _listSliver {
    List<Widget> list = [];
    for (var section = 0; section < widget.sectionCount; section++) {
      final header = widget.headerForSection?.call(section);
      if (header != null) {
        list.add(SliverToBoxAdapter(child: header));
      }

      list.add(SliverGrid.builder(
          addAutomaticKeepAlives: widget.addAutomaticKeepAlive,
          addRepaintBoundaries: widget.addRepaintBoundaries,
          addSemanticIndexes: widget.addSemanticIndexes,
          gridDelegate: widget.gridDelegate,
          itemBuilder: (context, index) => widget.itemInSectionBuilder.call(context, IndexPath(section: section, index: index)),
          itemCount: widget.itemInSectionCount.call(section)));

      final footer = widget.footerForSection?.call(section);
      if (footer != null) {
        list.add(SliverToBoxAdapter(child: footer));
      }
    }
    return list;
  }
}

class IndexPath {
  const IndexPath({required this.section, required this.index});

  final int section;
  final int index;
}
