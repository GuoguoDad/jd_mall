// Flutter imports:
import 'package:flutter/material.dart';
import 'package:module_flutter/common/extension/extensions.dart';

// Project imports:
import 'package:module_flutter/common/util/screen_util.dart';
import 'package:module_flutter/component/image/asset_image.dart';

Widget orderSummation(BuildContext context) {
  return SliverToBoxAdapter(
    child: Container(
      width: getScreenWidth(context),
      padding: const EdgeInsets.only(left: 20, top: 20, right: 20, bottom: 6),
      margin: const EdgeInsets.only(top: 12),
      decoration: const BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.all(Radius.circular(10)),
      ),
      child: Column(
        children: [
          rowItem("商品金额", "￥1600"),
          rowItem("退换货免运费", "￥0.00"),
          rowItem("运费", "￥0.00"),
          rowItem("立减", "￥100.00"),
          rowItem("PLUS 95折", "-￥20.00", valueColor: Colors.red),
          rowItem("优惠券", "-￥80.00", showArrow: true, valueColor: Colors.red),
          Container(
            margin: const EdgeInsets.only(top: 10),
            padding: const EdgeInsets.only(top: 16, bottom: 20, right: 20),
            decoration: BoxDecoration(
              border: Border(
                top: BorderSide(
                  width: 1, //宽度
                  color: "#F8F7F8".toColor(), //边框颜色
                ),
              ),
            ),
            child: const Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                Text(
                  "合计:",
                  style: TextStyle(fontSize: 16),
                ),
                Text(
                  "￥1480",
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.red),
                ),
              ],
            ),
          )
        ],
      ),
    ),
  );
}

Widget rowItem(String title, String value, {Color? valueColor, bool? showArrow}) {
  showArrow ??= false;
  valueColor ??= Colors.black;

  return Container(
    margin: const EdgeInsets.only(top: 8, bottom: 8),
    child: Row(
      children: [
        Expanded(
          flex: 1,
          child: Row(
            children: [
              Text(
                title,
                style: const TextStyle(fontSize: 16),
              )
            ],
          ),
        ),
        Expanded(
          flex: 1,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.end,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Text(
                value,
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: valueColor),
              ),
              showArrow
                  ? Container(
                      margin: const EdgeInsets.only(left: 10),
                      child: assetFillImage("images/ic_arrow_right_grey.png", 12, 14),
                    )
                  : Container(),
            ],
          ),
        )
      ],
    ),
  );
}
