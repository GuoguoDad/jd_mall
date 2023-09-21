// Flutter imports:
import 'package:flutter/material.dart';

// Project imports:
import 'package:module_flutter/common/extension/extensions.dart';
import 'package:module_flutter/common/util/screen_util.dart';
import 'package:module_flutter/component/linear_button.dart';

Widget fixedBottom(BuildContext context) {
  double space = getBottomSpace(context);

  return Container(
    height: 64 + space,
    padding: EdgeInsets.only(bottom: space),
    decoration: BoxDecoration(color: Colors.white, border: Border.all(color: Colors.grey.shade200, width: 0.1)),
    child: Row(
      children: [
        Expanded(
          flex: 3,
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.end,
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Container(
                margin: const EdgeInsets.only(left: 12),
                child: const Text("￥", style: TextStyle(fontSize: 20, color: Colors.red, fontWeight: FontWeight.w600, decoration: TextDecoration.none)),
              ),
              const Text("1480", style: TextStyle(fontSize: 30, fontWeight: FontWeight.w600, color: Colors.red, decoration: TextDecoration.none)),
              const Text(".00", style: TextStyle(fontSize: 20, fontWeight: FontWeight.w600, color: Colors.red, decoration: TextDecoration.none)),
            ],
          ),
        ),
        Expanded(
          flex: 4,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              LinearButton(
                width: 100,
                height: 48,
                btnName: "帮我付",
                highlightColor: Colors.yellow,
                colors: ["#F2CD4A".toColor(), "#F2C54B".toColor()],
                borderRadius: const BorderRadius.all(Radius.circular(50)),
                onTap: () => print("======="),
              ),
              LinearButton(
                width: 100,
                height: 48,
                btnName: "自己付",
                highlightColor: Colors.red,
                colors: ["#E54B4E".toColor(), "#E34439".toColor()],
                borderRadius: const BorderRadius.all(Radius.circular(50)),
                onTap: () => print("======="),
              )
            ],
          ),
        )
      ],
    ),
  );
}
