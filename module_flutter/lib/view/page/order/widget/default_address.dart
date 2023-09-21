// Flutter imports:
import 'package:flutter/material.dart';

// Project imports:
import 'package:module_flutter/common/util/screen_util.dart';
import 'package:module_flutter/component/image/asset_image.dart';

Widget defaultAddress(BuildContext context) {
  return SliverToBoxAdapter(
    child: Container(
      width: getScreenWidth(context),
      padding: const EdgeInsets.all(20),
      decoration: const BoxDecoration(
        color: Colors.white,
        // border: Border(bottom: BorderSide(width: 3, color: "#85A6F8".toColor())),
        borderRadius: BorderRadius.only(bottomLeft: Radius.circular(10), bottomRight: Radius.circular(10)),
      ),
      child: Row(
        children: [
          Expanded(
            flex: 1,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    tag("默认", Colors.red),
                    const SizedBox(width: 5),
                    tag("家", Colors.blue),
                    const SizedBox(width: 5),
                    const Text(
                      "江苏省南京市江宁区东山街道",
                      style: TextStyle(fontSize: 16, color: Colors.black, fontWeight: FontWeight.w300, decoration: TextDecoration.none),
                    )
                  ],
                ),
                Container(
                  margin: const EdgeInsets.only(top: 8),
                  child: const Text(
                    "丰泽路580号中粮详云18栋1807室",
                    style: TextStyle(fontSize: 16, color: Colors.black, fontWeight: FontWeight.w600, decoration: TextDecoration.none),
                  ),
                ),
                Container(
                  margin: const EdgeInsets.only(top: 8),
                  child: const Text(
                    "刘辉 199****8406",
                    style: TextStyle(fontSize: 16, color: Colors.black, fontWeight: FontWeight.w300, decoration: TextDecoration.none),
                  ),
                )
              ],
            ),
          ),
          Container(width: 20, alignment: Alignment.center, child: arrow)
        ],
      ),
    ),
  );
}

Widget tag(String text, Color bg) {
  return Container(
    width: 28,
    decoration: BoxDecoration(color: bg, borderRadius: const BorderRadius.all(Radius.circular(2))),
    alignment: Alignment.center,
    child: Text(
      text,
      style: const TextStyle(color: Colors.white, fontWeight: FontWeight.w500, fontSize: 12, decoration: TextDecoration.none),
    ),
  );
}

Widget arrow = assetFillImage("images/ic_arrow_right_grey.png", 10, 14);
