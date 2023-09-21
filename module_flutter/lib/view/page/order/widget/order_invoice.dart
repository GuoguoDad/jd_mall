// Flutter imports:
import 'package:flutter/material.dart';
import 'package:module_flutter/common/extension/extensions.dart';

// Project imports:
import 'package:module_flutter/common/util/screen_util.dart';
import 'package:module_flutter/component/image/asset_image.dart';

Widget orderInvoice(BuildContext context) {
  return SliverToBoxAdapter(
    child: Container(
      width: getScreenWidth(context),
      padding: const EdgeInsets.only(left: 20, top: 12, right: 20, bottom: 12),
      margin: const EdgeInsets.only(top: 12, bottom: 12),
      decoration: const BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.all(Radius.circular(10)),
      ),
      child: Column(
        children: [
          rowItem("发票", "电子(商品明细-个人)", showArrow: true, valueColor: "#2E2E2E".toColor()),
          rowItem("支付方式", "在线支付", showArrow: true, valueColor: "#2E2E2E".toColor()),
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
                style: TextStyle(fontSize: 16, color: valueColor),
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
