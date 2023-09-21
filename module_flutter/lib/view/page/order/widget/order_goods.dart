// Flutter imports:
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';

// Project imports:
import 'package:module_flutter/common/util/screen_util.dart';
import 'package:module_flutter/component/image/asset_image.dart';

double width = 80;

Widget orderGoods(BuildContext context) {
  return SliverToBoxAdapter(
    child: Container(
      width: getScreenWidth(context),
      padding: const EdgeInsets.only(left: 20, top: 20, right: 20, bottom: 12),
      margin: const EdgeInsets.only(top: 12),
      decoration: const BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.all(Radius.circular(10)),
      ),
      child: Column(
        children: [
          Row(
            children: [
              Container(child: assetImage("images/ic_store.png", 24, 24)),
              Container(margin: const EdgeInsets.only(left: 4), child: const Text("ASICS旗舰店", style: TextStyle(fontSize: 16))),
            ],
          ),
          Container(
            margin: const EdgeInsets.only(top: 24),
            child: Row(
              children: [
                Expanded(
                  flex: 1,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        children: [
                          goodsImg("https://ik.imagekit.io/guoguodad/mall/cart/s01g01.jpg?updatedAt=1691722797705"),
                          goodsImg("https://ik.imagekit.io/guoguodad/mall/cart/s01g02.jpeg?updatedAt=1691722798312"),
                        ],
                      ),
                    ],
                  ),
                ),
                Container(
                  width: 80,
                  alignment: Alignment.center,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      const Text("共2件"),
                      arrow,
                    ],
                  ),
                )
              ],
            ),
          ),
          rowItem("配送", "快递运输", "预计明天发货"),
          rowItem("退换货免运费", "商家赠送", "享运费补贴或免费取件服务"),
          messageInput(context)
        ],
      ),
    ),
  );
}

Widget goodsImg(String url) {
  return Container(
    margin: const EdgeInsets.only(right: 10),
    child: ClipRRect(
      borderRadius: const BorderRadius.all(Radius.circular(6)),
      child: CachedNetworkImage(
        width: width,
        height: width,
        imageUrl: url,
        placeholder: (context, url) => assetImage("images/default.png", width, width),
        errorWidget: (context, url, error) => assetImage("images/default.png", width, width),
        fit: BoxFit.fill,
      ),
    ),
  );
}

Widget rowItem(String title, String value, String? des) {
  return Container(
    margin: const EdgeInsets.only(top: 12, bottom: 12),
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
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              Text(
                value,
                style: const TextStyle(fontSize: 16),
              ),
              Text(
                des ?? '',
                style: const TextStyle(fontSize: 16),
              ),
            ],
          ),
        )
      ],
    ),
  );
}

Widget messageInput(BuildContext context) {
  return Row(
    mainAxisAlignment: MainAxisAlignment.spaceBetween,
    children: [
      const Text(
        "留言",
        style: TextStyle(fontSize: 16),
      ),
      SizedBox(
        width: getScreenWidth(context) * 2 / 3,
        child: const TextField(
          textAlign: TextAlign.right,
          cursorColor: Colors.black45,
          decoration: InputDecoration(hintText: "建议留言前先与商家沟通确认", border: InputBorder.none),
          style: TextStyle(
            fontSize: 16,
          ),
        ),
      ),
    ],
  );
}

Widget arrow = assetFillImage("images/ic_arrow_right_grey.png", 10, 14);
