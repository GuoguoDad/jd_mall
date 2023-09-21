// Flutter imports:
import 'package:flutter/material.dart';

// Project imports:
import 'package:module_flutter/common/style/common_style.dart';

class CommonIndicator extends StatelessWidget {
  final int? itemCount;
  final int? current;

  const CommonIndicator({super.key, this.itemCount, this.current});

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      scrollDirection: Axis.horizontal,
      itemCount: itemCount,
      itemBuilder: (context, index) {
        return Container(
          alignment: const Alignment(0, .5),
          height: 10,
          width: 10,
          child: CircleAvatar(
            radius: 3,
            backgroundColor: current == index ? CommonStyle.themeColor : Colors.grey,
            child: Container(
              alignment: const Alignment(0, .5),
              width: 10,
              height: 10,
            ),
          ),
        );
      },
    );
  }
}
